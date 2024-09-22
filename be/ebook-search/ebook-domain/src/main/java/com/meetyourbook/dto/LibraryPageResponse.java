package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;
import java.util.List;
import org.springframework.data.domain.Page;

public record LibraryPageResponse (
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    List<LibraryResponse> content
){

    public static LibraryPageResponse of(Page<Library> page) {
        List<LibraryResponse> content = page.getContent().stream()
            .map(LibraryResponse::fromEntity)
            .toList();

        return new LibraryPageResponse(page.getNumber(), page.getSize(), page.getTotalElements(),
            page.getTotalPages(), content);
    }

}
