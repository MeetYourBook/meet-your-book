package com.meetyourbook.crawler;

import static com.meetyourbook.service.BookCrawlerService.pageQueue;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.parser.BookPageParser;
import com.meetyourbook.service.BookQueueService;
import io.micrometer.core.instrument.Counter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

@Component("BookPage")
@Slf4j
@RequiredArgsConstructor
public class BookPageProcessor implements PageProcessor {

    private final Site site = Site.me().setTimeOut(10000000).setSleepTime(8000)
        .addHeader("Accept-Encoding", "gzip, deflate, br");
    private final BookQueueService bookQueueService;
    private final Counter pagesCounter;
    private final Counter booksCounter;
    private final BookPageParser bookPageParser;

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        String url = page.getUrl().get();
        List<BookInfo> bookInfos = bookPageParser.parseBooks(page.getHtml().getDocument());
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
