package com.meetyourbook.dto;

import java.util.List;

public record BookSearchRequest(
    String title,
    String author,
    String publisher,
    List<Long> libraries,
    Integer page,
    Integer size,
    String sort

) {

    public BookSearchRequest {
        sort = sort != null ? sort : "publishDate";
        page = page != null ? page : 0;
        size = size != null ? size : 10;

    }

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
