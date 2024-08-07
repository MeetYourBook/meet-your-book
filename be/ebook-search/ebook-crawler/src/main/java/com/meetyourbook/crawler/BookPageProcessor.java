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
import java.util.List;
import java.util.Optional;
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

@Component
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

    private final Site site = Site.me().setTimeOut(10000000).setSleepTime(8000)
        .addHeader("Accept-Encoding", "gzip, deflate, br");
    private final BookQueueService bookQueueService;
    private final Counter pagesCounter;
    private final Counter booksCounter;

    private String baseUrl;

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
        baseUrl = new LibraryUrl(page.getUrl().get()).getBaseUrl();
        log.info("사이트 이름: {}", baseUrl);

        Document doc = page.getHtml().getDocument();

        List<BookInfo> bookInfos = parseBooks(doc);
        log.info("파싱된 책의 개수: {}", bookInfos.size());

        if (!bookInfos.isEmpty()) {
            bookQueueService.addBookInfosToQueue(bookInfos);
            booksCounter.increment(bookInfos.size());
            pagesCounter.increment();

            try {
                String nextUrl = fetchNextUrl(page.getUrl().get());
                pageQueue.add(nextUrl);
            } catch (URISyntaxException e) {
                log.error("다음 페이지 URL을 가져오는 과정에서 오류가 생김", e);
            }
        }

    }

    private List<BookInfo> parseBooks(Document doc) {
        Element resultListElement = doc.getElementsByClass(RESULT_LIST_CLASS).first();
        if (resultListElement == null) {
            log.warn("Result list element not found");
            return List.of();
        }

        Elements bookElements = getBookElements(resultListElement);

        return bookElements.stream()
            .map(this::parseBookInfo)
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

    private Optional<BookInfo> parseBookInfo(Element bookElement) {
        try {
            Element writerElement = bookElement.getElementsByClass(WRITER_CLASS).first();
            if (writerElement == null) {
                log.warn("Writer element not found for book");
                return Optional.empty();
            }

            return Optional.of(BookInfo.builder()
                .provider(getTextFromClass(bookElement, PROVIDER_CLASS))
                .title(getTextFromClass(bookElement, TITLE_CLASS))
                .author(getAuthor(writerElement))
                .publisher(getPublisher(writerElement))
                .publishDate(getPublishDate(writerElement))
                .imageUrl(getImgUrl(bookElement))
                .baseUrl(baseUrl)
                .build());
        } catch (Exception e) {
            log.error("Error parsing book info", e);
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
            .map(node -> LocalDate.parse(node.text(), DATE_FORMATTER))
            .orElse(null);
    }

    private String getImgUrl(Element element) {
        return Optional.ofNullable(element.getElementsByTag(IMG_TAG).first())
            .map(img -> img.attr(SRC_ATTR))
            .map(url -> url.startsWith("//") ? url.substring(2) : url)
            .orElse(NONE_DATA);
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
        log.info("newUri: {}", newUri);

        return newUri.toString();
    }
}
