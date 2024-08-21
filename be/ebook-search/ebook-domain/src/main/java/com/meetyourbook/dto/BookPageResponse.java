package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
import java.util.List;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record BookPageResponse(int pageNumber, int pageSize, long totalElements, int totalPages,
                               List<SimpleBookResponse> content) {

    public static BookPageResponse of(Page<Book> page) {
        List<SimpleBookResponse> simpleBookResponses = page.getContent().stream()
            .map(SimpleBookResponse::fromEntity).toList();

        return new BookPageResponse(page.getNumber(), page.getSize(), page.getTotalElements(),
            page.getTotalPages(), simpleBookResponses);
    }
}