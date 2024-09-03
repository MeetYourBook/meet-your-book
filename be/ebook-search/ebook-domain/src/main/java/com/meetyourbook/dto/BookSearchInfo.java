package com.meetyourbook.dto;

import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Builder
public record BookSearchInfo(
    String title,
    String author,
    String publisher,
    List<Long> libraries,
    int page,
    int size,
    String sort

) {

    public Pageable toPageable() {
        Sort sort = Sort.by(Direction.DESC, this.sort);
        return PageRequest.of(page, size, sort);
    }

}
