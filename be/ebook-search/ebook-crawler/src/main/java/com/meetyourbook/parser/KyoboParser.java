package com.meetyourbook.parser;

import com.meetyourbook.config.KyoboParserConfig;
import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.LibraryUrl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KyoboParser implements BookPageParser {

    private static final String NONE_DATA = "";
    private static final String SRC_ATTR = "src";
    private static final String I_TAG = "i";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final Pattern ONCLICK_PATTERN = Pattern.compile(
        "fnContentClick\\(this,\\s*'(\\d+)',\\s*'([^']+)',\\s*'(\\d+)',\\s*'([^']*)'");
    private static final String BOOK_URL_QUERY_FORMAT = "cttsDvsnCode=%s&brcd=%s&sntnAuthCode=%s&ctgrId=%s";

    private final KyoboParserConfig kyoboParserConfig;


    @Override
    public List<BookInfo> parseBooks(Document doc) {
        return Optional.ofNullable(
                doc.getElementsByClass(kyoboParserConfig.getBookSelector()).first())
            .map(this::getBookElements)
            .map(elements -> parseBookInfos(elements, new LibraryUrl(doc.location()).getBaseUrl()))
            .orElseGet(() -> {
                log.warn("페이지에 책 목록이 없습니다. URL: {}", doc.location());
                return List.of();
            });
    }

    private List<BookInfo> parseBookInfos(Elements bookElements, String baseUrl) {
        return bookElements.stream()
            .flatMap(bookElement -> parseBookInfo(bookElement, baseUrl).stream())
            .toList();
    }

    private Optional<BookInfo> parseBookInfo(Element bookElement, String baseUrl) {
        try {
            return Optional.of(BookInfo.builder()
                .title(parseTitle(bookElement))
                .author(parseAuthor(bookElement))
                .publisher(parsePublisher(bookElement))
                .publishDate(parsePublishDate(bookElement))
                .imageUrl(parseImageUrl(bookElement))
                .bookUrl(parseBookUrl(bookElement, baseUrl))
                .baseUrl(baseUrl)
                .build());
        } catch (Exception e) {
            log.error("책 정보 파싱 중 오류 발생: {}, URL: {}", e.getMessage(), baseUrl);
            return Optional.empty();
        }
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
        log.info("페이지에서 발견된 도서 요소 수: {}", bookElements.size());
        return bookElements;
    }

    private String parseTitle(Element bookElement) {
        return Optional.ofNullable(bookElement.selectFirst(kyoboParserConfig.getTitleSelector()))
            .map(Element::text)
            .orElse(NONE_DATA);
    }

    private String parseAuthor(Element bookElement) {
        return Optional.ofNullable(bookElement.selectFirst(kyoboParserConfig.getAuthorSelector()))
            .flatMap(element -> element.textNodes().stream().findFirst())
            .map(node -> node.text().trim())
            .orElse(NONE_DATA);
    }

    private String parsePublisher(Element bookElement) {
        return Optional.ofNullable(
                bookElement.selectFirst(kyoboParserConfig.getPublisherSelector()))
            .map(Element::text)
            .orElse(NONE_DATA);
    }

    private LocalDate parsePublishDate(Element bookElement) {
        return Optional.ofNullable(
                bookElement.selectFirst(kyoboParserConfig.getPublishDateSelector()))
            .flatMap(element -> element.textNodes().stream().skip(1).findFirst())
            .map(node -> node.text().trim())
            .map(this::parseDate)
            .orElse(LocalDate.now());
    }

    private String parseImageUrl(Element bookElement) {
        return Optional.ofNullable(bookElement.selectFirst(kyoboParserConfig.getImageSelector()))
            .map(img -> img.attr(SRC_ATTR))
            .map(url -> url.startsWith("//") ? url.substring(2) : url)
            .orElse(NONE_DATA);
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            log.warn("날짜 파싱 오류. 입력 데이터: {}, 오류 원인: {}", date, e.getMessage());
            return LocalDate.now();
        }
    }

    private String parseBookUrl(Element bookElement, String baseUrl) {
        return Optional.ofNullable(bookElement.selectFirst("a[onclick*=fnContentClick]"))
            .map(linkElement -> {
                Matcher matcher = ONCLICK_PATTERN.matcher(linkElement.attr("onclick"));
                if (matcher.find()) {
                    String queryString = String.format(BOOK_URL_QUERY_FORMAT,
                        matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3));
                    return baseUrl + linkElement.attr("href") + "?" + queryString;
                }
                return NONE_DATA;
            })
            .orElse(NONE_DATA);
    }

}