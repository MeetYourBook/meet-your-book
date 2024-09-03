package com.meetyourbook.dto;

import jakarta.validation.constraints.NotNull;

public record EbookPlatformCrawlRequest(

    @NotNull
    String ebookPlatform,

    String url

) {

}
