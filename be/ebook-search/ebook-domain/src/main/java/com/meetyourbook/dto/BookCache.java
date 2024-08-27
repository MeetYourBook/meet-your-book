package com.meetyourbook.dto;

import java.time.LocalDate;

public record BookCache(
    String title,
    String author,
    String publisher,
    LocalDate publishDate
) {

}
