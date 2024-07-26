package com.meetyourbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EbookCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbookCrawlerApplication.class, args);
    }

}
