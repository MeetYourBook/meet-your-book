package com.meetyourbook.dto;

import java.util.List;

public record BookSearchRequest(
    String title,
    String author,
    String publisher,
    List<Long> libraries,
    int page,
    int size,
    String sort

) {

    public BookSearchInfo toBookSearchInfo() {
        return BookSearchInfo.builder()
            .title(title)
            .author(author)
            .publisher(publisher)
            .libraries(libraries)
            .page(page)
            .size(size)
            .sort(sort)
            .build();
    }

}
