package com.meetyourbook;

import com.meetyourbook.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LibraryDataImporter {

    private static final Logger log = LoggerFactory.getLogger(LibraryDataImporter.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LibraryDataImporter.class, args);

        LibraryService libraryService = context.getBean(LibraryService.class);
        libraryService.saveLibraryFromJson("/Users/jeonbyeong-ung/IdeaProjects/meet-your-book/be/ebook-search/ebook-crawler/src/main/resources/library_list.json");

        log.info("도서관 데이터 저장 완료");

        SpringApplication.exit(context);
    }

}
