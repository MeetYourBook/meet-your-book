package com.meetyourbook.crawler.library;

import com.meetyourbook.common.annotation.EbookPlatformCrawler;
import com.meetyourbook.entity.Library.EbookPlatform;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LibraryCrawlerFactory {

    private final Map<EbookPlatform, EbookPlatformLibraryCrawler> crawlerMap;

    @Autowired
    public LibraryCrawlerFactory(List<EbookPlatformLibraryCrawler> libraryCrawlers) {
        this.crawlerMap = new EnumMap<>(EbookPlatform.class);
        initializeCrawlerMap(libraryCrawlers);
    }

    public EbookPlatformLibraryCrawler findByPlatformName(EbookPlatform platform) {
        return crawlerMap.get(platform);
    }

    private void initializeCrawlerMap(List<EbookPlatformLibraryCrawler> libraryCrawlers) {
        for (EbookPlatformLibraryCrawler libraryCrawler : libraryCrawlers) {
            EbookPlatformCrawler annotation = AnnotatedElementUtils.findMergedAnnotation(
                libraryCrawler.getClass(), EbookPlatformCrawler.class);
            if (annotation != null) {
                crawlerMap.put(annotation.value(), libraryCrawler);
            } else {
                log.warn("EbookPlatformCrawler 애노테이션이 적용되지 않은 클래스: {}",
                    libraryCrawler.getClass().getName());
            }

        }
    }

}
