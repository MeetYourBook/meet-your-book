package com.meetyourbook.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookUpdateRequest(

    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Author is required")
    String author,

    @NotBlank(message = "Publisher is required")
    String publisher,

    @NotBlank(message = "Provider is required")
    String provider,

    LocalDate publishDate,
    String imageUrl

) {

}
