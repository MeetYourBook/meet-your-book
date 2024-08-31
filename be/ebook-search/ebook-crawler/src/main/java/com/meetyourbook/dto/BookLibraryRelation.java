package com.meetyourbook.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookLibraryRelation(

    UUID bookId,
    Long libraryId,
    String bookUrl,
    LocalDateTime dateTime

) {

    public BookLibraryRelation(UUID bookId, Long libraryId, String bookUrl) {
        this(bookId, libraryId, bookUrl, LocalDateTime.now());
    }
}
