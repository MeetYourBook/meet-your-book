package com.meetyourbook.dto;

import com.meetyourbook.entity.Library.EbookPlatform;

public record LibraryCrawlerTarget(

    EbookPlatform platform,
    String url

) {

}
