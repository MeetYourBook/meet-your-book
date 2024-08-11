package com.meetyourbook.crawler;

import com.meetyourbook.entity.LibraryUrl;
import com.meetyourbook.service.LibraryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@Component("BookCount")
@RequiredArgsConstructor
public class BookCountProcessor implements PageProcessor {

    private static final String BOOK_RESULT_TXT_CLASS = "book_resultTxt";
    private static final String STRONG_TAG = "strong";

    private final Site site = Site.me().setTimeOut(10000000).setSleepTime(8000)
        .setCycleRetryTimes(3).setRetryTimes(3);

    private final LibraryService libraryService;

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String baseUrl = new LibraryUrl(page.getUrl().get()).getBaseUrl();
        Document doc = page.getHtml().getDocument();

        int totalBookCount = getTotalBookCount(doc);
        log.info("도서관 URL: {}, 책의 총 개수: {}", baseUrl, totalBookCount);

        libraryService.updateTotalBookCount(totalBookCount, baseUrl);

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
}
