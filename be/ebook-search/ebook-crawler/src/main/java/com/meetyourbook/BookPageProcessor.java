package com.meetyourbook;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
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
    private static final String NONE_DATA = "";
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        Document doc = Jsoup.parse(page.getHtml().toString());
        Elements bookElements = Objects.requireNonNull(doc.getElementsByClass(RESULT_LIST_CLASS).first()).children();

        List<BookInfo> books = bookElements.stream()
            .map(this::parseBookInfo)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .peek(bookInfo -> log.info("bookInfo: {}", bookInfo))
            .toList();

    }

    private Optional<BookInfo> parseBookInfo(Element bookElement) {
        try {
            Element writerElement = bookElement.getElementsByClass("writer").first();
            return Optional.of(BookInfo.builder()
                .provider(getProvider(bookElement))
                .title(getTitle(bookElement))
                .author(getAuthor(writerElement))
                .publisher(getPublisher(writerElement))
                .publishDate(getPublishDate(writerElement))
                .imageURL(getImgUrl(bookElement))
                .build());
        } catch (Exception e) {
            log.error("parseBookInfo error", e);
            return Optional.empty();
        }
    }


    private String getTextFromClass(Element element, String className) {
        Element targetElement = element.getElementsByClass(className).first();
        if (targetElement != null) {
            return targetElement.text();
        }
        return NONE_DATA;
    }

    private String getProvider(Element element) {
        return getTextFromClass(element, PROVIDER_CLASS);
    }

    private String getTitle(Element element) {
        return getTextFromClass(element, TITLE_CLASS);
    }

    private String getAuthor(Element writerElement) {
        return writerElement.textNodes().get(0).text();
    }

    private String getPublisher(Element writerElement) {
        return writerElement.getElementsByTag("span").first().text().trim();
    }

    private LocalDate getPublishDate(Element writerElement) {
        String publishDate = writerElement.textNodes().get(1).text();
        return LocalDate.parse(publishDate, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String getImgUrl(Element element) {
        return element.getElementsByTag("img").first().attr("src").substring(2);
    }


}
