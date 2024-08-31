package com.meetyourbook.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record BookRecord(

    String title,
    String author,
    String publisher,
    LocalDate publishDate,
    String provider,
    String imageUrl,
    String description,
    String baseUrl,
    String bookUrl,
    LocalDateTime dateTime

) {

    public static BookRecord from(BookInfo bookInfo) {
        return BookRecord.builder()
            .title(bookInfo.title())
            .author(bookInfo.author())
            .publisher(bookInfo.publisher())
            .publishDate(bookInfo.publishDate())
            .provider(bookInfo.provider())
            .imageUrl(bookInfo.imageUrl())
            .description(bookInfo.description())
            .baseUrl(bookInfo.baseUrl())
            .bookUrl(bookInfo.bookUrl())
            .dateTime(LocalDateTime.now())
            .build();
    }

}
