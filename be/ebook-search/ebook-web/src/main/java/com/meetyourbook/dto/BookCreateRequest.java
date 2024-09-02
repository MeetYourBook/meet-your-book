package com.meetyourbook.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookCreateRequest(

    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Author is required")
    String author,

    @NotBlank(message = "Publisher is required")
    String publisher,

    LocalDate publishDate,
    String url

) {

    public BookCreationInfo toBookCreation() {
        return BookCreationInfo.builder()
            .title(title)
            .author(author)
            .publisher(publisher)
            .publishDate(publishDate)
            .url(url)
            .build();
    }

}
