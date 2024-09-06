package com.meetyourbook.dto;

import java.time.LocalDate;

public record BookUniqueKey(
    String title,
    String author,
    String publisher,
    LocalDate publishDate
) {

    public static BookUniqueKey from(BookRecord bookRecord) {
        return new BookUniqueKey(bookRecord.title(), bookRecord.author(),
            bookRecord.publisher(), bookRecord.publishDate());
    }

}
