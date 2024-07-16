package com.meetyourbook.crawler;

import java.util.ArrayList;
import java.util.List;
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
        List<String> urls = List.of("https://snu.dkyobobook.co.kr/elibrary-front/content/contentList.ink?cttsDvsnCode=001");
        String[] arrays = urls.toArray(new String[0]);

        Spider.create(bookPageProcessor)
            .addUrl(arrays)
            .thread(5)
            .run();

    }
}
