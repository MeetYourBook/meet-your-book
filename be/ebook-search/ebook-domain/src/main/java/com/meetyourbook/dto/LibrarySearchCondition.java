package com.meetyourbook.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public record LibrarySearchCondition (
    String name,
    Pageable pageable
) {

    public static LibrarySearchCondition from(String name, Integer page, Integer size) {
        Sort sort = Sort.by(Direction.ASC, "name");
        return new LibrarySearchCondition(name, PageRequest.of(page, size, sort));
    }

}
