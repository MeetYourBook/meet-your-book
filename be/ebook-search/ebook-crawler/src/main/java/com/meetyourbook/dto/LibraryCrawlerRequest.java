package com.meetyourbook.dto;

public record LibraryCrawlerRequest(

    String libraryBaseUrl,
    String savePath,
    int crawlInterval

) {

}
