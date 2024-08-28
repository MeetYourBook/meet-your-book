package com.meetyourbook.dto;

public record LibraryCrawlResult(

    String libraryCode,
    String libraryName,
    String libraryHost,
    String drmHost,
    String pushYn,
    String b2bType,
    String apiUrl

) {

}
