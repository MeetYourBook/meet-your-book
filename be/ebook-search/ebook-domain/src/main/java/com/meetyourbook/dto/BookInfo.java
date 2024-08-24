package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookInfo(

    String title,
    String author,
    String publisher,
    LocalDate publishDate,
    String provider,
    String imageUrl,
    String description,
    String baseUrl,
    String bookUrl

) {

    public Book toEntity() {
        return Book.builder()
            .title(title)
            .author(author)
            .publisher(publisher)
            .publishDate(publishDate)
            .provider(provider)
            .imageUrl(imageUrl)
            .build();
    }

}
