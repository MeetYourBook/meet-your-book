package com.meetyourbook.dto;

import java.time.LocalDate;

public record BookUniqueKey(
    String title,
    String author,
    String publisher,
    LocalDate publishDate
) {

    public static BookUniqueKey from(BookInfo bookInfo) {
        return new BookUniqueKey(bookInfo.title(), bookInfo.author(),
            bookInfo.publisher(), bookInfo.publishDate());
    }

}
