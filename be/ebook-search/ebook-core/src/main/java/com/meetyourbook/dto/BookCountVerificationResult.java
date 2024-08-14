package com.meetyourbook.dto;

public record BookCountVerificationResult(

    Long libraryId,
    String libraryName,
    int actualBookCount,
    int expectedBookCount,
    boolean verified

) {

}
