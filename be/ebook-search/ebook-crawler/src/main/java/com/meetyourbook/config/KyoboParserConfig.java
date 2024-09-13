package com.meetyourbook.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kyobo.parser")
@Data
public class KyoboParserConfig {

    private String bookSelector = "book_resultList";
    private String titleSelector = ".tit";
    private String authorSelector = ".writer";
    private String publisherSelector = ".writer span";
    private String publishDateSelector = ".writer span:last-child";
    private String imageSelector = "img";
    private String bookUrlSelector = ".tit a";
    private String nextPageSelector = ".next_page";
}
