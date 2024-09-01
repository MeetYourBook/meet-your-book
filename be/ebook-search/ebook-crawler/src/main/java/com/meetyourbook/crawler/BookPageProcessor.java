package com.meetyourbook.crawler;

import static com.meetyourbook.service.BookCrawlerService.pageQueue;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.LibraryUrl;
import com.meetyourbook.service.BookQueueService;
import io.micrometer.core.instrument.Counter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Component("BookPage")
@Slf4j
public class BookPageProcessor implements PageProcessor {

    private static final String RESULT_LIST_CLASS = "book_resultList";
    private static final String PROVIDER_CLASS = "store";
    private static final String TITLE_CLASS = "tit";
    private static final String WRITER_CLASS = "writer";
    private static final String NONE_DATA = "";
    private static final String IMG_TAG = "img";
    private static final String SRC_ATTR = "src";
    private static final String SPAN_TAG = "span";
    private static final String I_TAG = "i";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Pattern ONCLICK_PATTERN = Pattern.compile(
        "fnContentClick\\(this,\\s*'(\\d+)',\\s*'([^']+)',\\s*'\\d+'");

    private final Site site = Site.me().setTimeOut(10000000).setSleepTime(8000)
        .addHeader("Accept-Encoding", "gzip, deflate, br");
    private final BookQueueService bookQueueService;
    private final Counter pagesCounter;
    private final Counter booksCounter;

    @Autowired
    public BookPageProcessor(BookQueueService bookQueueService,
        @Qualifier("pagesCounter") Counter pagesCounter,
        @Qualifier("booksCounter") Counter booksCounter) {
        this.bookQueueService = bookQueueService;
        this.pagesCounter = pagesCounter;
        this.booksCounter = booksCounter;
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        List<BookInfo> bookInfos = parseBooks(page);

        if (!bookInfos.isEmpty()) {
            bookQueueService.addBookInfosToQueue(bookInfos);
            booksCounter.increment(bookInfos.size());
            pagesCounter.increment();
            log.info("페이지 처리 완료: {}, 발견된 도서 수: {}", url, bookInfos.size());

            try {
                String nextUrl = fetchNextUrl(page.getUrl().get());
                pageQueue.add(nextUrl);
            } catch (URISyntaxException e) {
                log.error("다음 페이지 URL 생성 중 오류 발생. 현재 URL: {}, 오류 메시지: {}", url, e.getMessage());
            }
        }

    }

    private List<BookInfo> parseBooks(Page page) {
        String baseUrl = new LibraryUrl(page.getUrl().get()).getBaseUrl();
        String exactPageUrl = page.getUrl().get();
        Document doc = page.getHtml().getDocument();

        Element resultListElement = doc.getElementsByClass(RESULT_LIST_CLASS).first();
        if (resultListElement == null) {
            log.warn("해당 페이지에서 도서 목록을 찾을 수 없습니다 : {}", exactPageUrl);
            return List.of();
        }

        Elements bookElements = getBookElements(resultListElement);
        log.debug("페이지에서 발견된 도서 요소 수: {}, 페이지 URL: {}", bookElements.size(), exactPageUrl);

        return bookElements.stream()
            .map(bookElement -> parseBookInfo(bookElement, baseUrl))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    private Elements getBookElements(Element resultListElement) {
        Elements bookElements = new Elements();
        for (Element child : resultListElement.children()) {
            if (I_TAG.equals(child.tagName())) {
                bookElements.addAll(child.children());
            } else {
                bookElements.add(child);
            }
        }
        return bookElements;
    }

    private Optional<BookInfo> parseBookInfo(Element bookElement, String baseUrl) {
        try {
            Element writerElement = bookElement.getElementsByClass(WRITER_CLASS).first();
            if (writerElement == null) {
                log.debug("도서의 작가 정보를 찾을 수 없음");
                return Optional.empty();
            }

            BookInfo bookInfo = BookInfo.builder()
                .title(getTextFromClass(bookElement, TITLE_CLASS))
                .author(getAuthor(writerElement))
                .publisher(getPublisher(writerElement))
                .publishDate(getPublishDate(writerElement))
                .imageUrl(getImgUrl(bookElement))
                .baseUrl(baseUrl)
                .bookUrl(getBookUrl(bookElement, baseUrl))
                .build();

            log.trace("파싱된 도서 정보: {}", bookInfo);
            return Optional.of(bookInfo);
        } catch (Exception e) {
            log.error("도서 정보 파싱 중 오류 발생: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private String getTextFromClass(Element element, String className) {
        return Optional.ofNullable(element.getElementsByClass(className).first())
            .map(Element::text)
            .orElse(NONE_DATA);
    }

    private String getAuthor(Element writerElement) {
        return writerElement.textNodes().stream()
            .findFirst()
            .map(node -> node.text().trim())
            .orElse(NONE_DATA);
    }

    private String getPublisher(Element writerElement) {
        return Optional.ofNullable(writerElement.getElementsByTag(SPAN_TAG).first())
            .map(element -> element.text().trim())
            .orElse(NONE_DATA);
    }

    private LocalDate getPublishDate(Element writerElement) {
        return writerElement.textNodes().stream()
            .skip(1)
            .findFirst()
            .map(node -> parseDate(node.text().trim()))
            .orElse(null);
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("날짜 파싱 오류. 입력 데이터: {}, 오류 원인: {}", date, e.getMessage());
            return null;
        }
    }

    private String getImgUrl(Element element) {
        return Optional.ofNullable(element.getElementsByTag(IMG_TAG).first())
            .map(img -> img.attr(SRC_ATTR))
            .map(url -> url.startsWith("//") ? url.substring(2) : url)
            .orElse(NONE_DATA);
    }

    private String getBookUrl(Element element, String baseUrl) throws URISyntaxException {
        Element titleElement = element.getElementsByClass(TITLE_CLASS).first();
        if (titleElement == null) {
            log.warn("도서 제목 요소를 찾을 수 없음");
            return NONE_DATA;
        }

        Element anchorElement = titleElement.getElementsByTag("a").first();
        if (anchorElement == null) {
            log.warn("도서 링크 요소를 찾을 수 없음");
            return NONE_DATA;
        }

        String onClickAttr = anchorElement.attr("onClick");
        String extraUrl = anchorElement.attr("href");
        Matcher matcher = ONCLICK_PATTERN.matcher(onClickAttr);

        if (matcher.find()) {
            String cttsDvsnCode = matcher.group(1);
            String brcd = matcher.group(2);

            URI uri = URI.create(baseUrl + extraUrl);
            String query = uri.getQuery();
            StringBuilder newQuery = new StringBuilder(query == null ? "" : query);

            if (!newQuery.isEmpty()) {
                newQuery.append("&");
            }
            newQuery.append("brcd=").append(brcd).append("&cttsDvsnCode=").append(cttsDvsnCode);

            URI newUri = new URI(
                uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                newQuery.toString(),
                uri.getFragment()
            );

            return newUri.toString();
        } else {
            log.warn("onClick 속성에서 매개변수를 추출할 수 없음. base URL: {}, extra URL: {}", baseUrl, extraUrl);
            return NONE_DATA;
        }
    }

    private String fetchNextUrl(String url) throws URISyntaxException {
        URI uri = URI.create(url);
        String query = uri.getQuery();
        String[] params = query.split("&");
        StringBuilder newQuery = new StringBuilder();

        for (String param : params) {
            if (param.startsWith("pageIndex=")) {
                String[] keyValue = param.split("=");
                int pageIndex = Integer.parseInt(keyValue[1]);
                pageIndex++;
                newQuery.append("pageIndex=").append(pageIndex).append("&");
            } else {
                newQuery.append(param).append("&");
            }
        }

        if (!newQuery.isEmpty()) {
            newQuery.setLength(newQuery.length() - 1);
        }

        URI newUri = new URI(
            uri.getScheme(),
            uri.getAuthority(),
            uri.getPath(),
            newQuery.toString(),
            uri.getFragment()
        );

        return newUri.toString();
    }
}
