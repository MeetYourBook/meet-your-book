package com.meetyourbook.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record BookUpdateInfo(

    String title,
    String author,
    String publisher,
    String provider,
    LocalDate publishDate,
    String imageUrl

) {


}
