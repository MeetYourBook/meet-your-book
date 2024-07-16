package com.meetyourbook.crawler;

import com.meetyourbook.dto.BookInfo;
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
    private static final String BOOK_RESULT_TXT_CLASS = "book_resultTxt";
    private static final String STRONG_TAG = "strong";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private final Site site = Site.me().setTimeOut(10000000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        log.info("사이트 이름: {}", getSite().getDomain());
        Document doc = page.getHtml().getDocument();

        log.info("총 책의 수: {}", getTotalBookCount(doc));

        List<BookInfo> books = parseBooks(doc);
        log.info("파싱된 책의 개수: {}", books.size());

        if (books.isEmpty()) {
            return;
        }

        try {
            page.addTargetRequest(fetchNextUrl(page.getUrl().toString()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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
            .peek(bookInfo -> log.info("book: {}", bookInfo))
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
                .imageURL(getImgUrl(bookElement))
                .build());
        } catch (Exception e) {
            log.error("Error parsing book info", e);
            return Optional.empty();
        }
    }

    private int getTotalBookCount(Document document) {
        Element bookResultTxt = document.getElementsByClass(BOOK_RESULT_TXT_CLASS).first();
        if (bookResultTxt == null) {
            log.warn("BookResultText가 존재하지 않습니다.");
            return 0;
        }

        Element strongElement = bookResultTxt.getElementsByTag(STRONG_TAG).first();
        if (strongElement == null) {
            log.warn("BookResultText에서 Strong태그 Element가 없음");
            return 0;
        }

        String countText = strongElement.text().replaceAll("\\D", "");
        try {
            return Integer.parseInt(countText);
        } catch (NumberFormatException e) {
            log.error("책의 총개수를 파싱하는 과정에서 오류가 생김", e);
            return 0;
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

        return newUri.toString();
    }


}
