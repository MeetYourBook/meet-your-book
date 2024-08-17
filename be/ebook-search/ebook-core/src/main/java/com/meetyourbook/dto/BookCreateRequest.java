package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
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

    @NotBlank(message = "Provider is required")
    String provider,

    LocalDate publishDate,
    String url

) {

    public Book toEntity() {
        return Book.builder().title(title).author(author).publisher(publisher).provider(provider)
            .publishDate(publishDate).imageUrl(url).build();
    }

}
