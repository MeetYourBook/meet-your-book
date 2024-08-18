package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookCreationInfo(

    String title,
    String author,
    String publisher,
    String provider,
    String url,
    LocalDate publishDate

) {

    public Book toEntity() {
        return Book.builder()
            .title(title)
            .author(author)
            .publisher(publisher)
            .provider(provider)
            .publishDate(publishDate)
            .imageUrl(url)
            .build();
    }

}
