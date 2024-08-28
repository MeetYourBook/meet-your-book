package com.meetyourbook.dto;

public record LibraryCrawlResponse(

    String resultCode,
    String resultMsg,
    String elapsedTime,
    String title,
    String description,
    LibraryCrawlMetaData resultData

) {

}
