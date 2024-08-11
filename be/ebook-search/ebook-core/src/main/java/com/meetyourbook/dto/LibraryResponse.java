package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;

public record LibraryResponse (
    Long id,
    String name
) {

    public static LibraryResponse fromEntity(Library library) {
        return new LibraryResponse(library.getId(), library.getName());
    }

}
