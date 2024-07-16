package com.meetyourbook.crawler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
@RequiredArgsConstructor
public class BookCrawlerRunner implements CommandLineRunner {

    private final BookPageProcessor bookPageProcessor;

    @Override
    public void run(String... args) throws Exception {
        Spider.create(bookPageProcessor)
            .addUrl("https://snu.dkyobobook.co.kr/elibrary-front/content/contentList.ink?cttsDvsnCode=001")
            .thread(5)
            .run();

    }
}
