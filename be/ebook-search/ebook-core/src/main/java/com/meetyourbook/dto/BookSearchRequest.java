package com.meetyourbook.dto;

import java.util.List;

public record BookSearchRequest(
    String title,
    String author,
    String publisher,
    List<Long> libraryIds) {
}
