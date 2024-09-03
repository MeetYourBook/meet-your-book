package com.meetyourbook.dto;

import java.time.LocalDateTime;

public record BookLibraryRelation(

    Long bookId,
    Long libraryId,
    String bookUrl,
    LocalDateTime dateTime

) {

    public BookLibraryRelation(Long bookId, Long libraryId, String bookUrl) {
        this(bookId, libraryId, bookUrl, LocalDateTime.now());
    }
}
