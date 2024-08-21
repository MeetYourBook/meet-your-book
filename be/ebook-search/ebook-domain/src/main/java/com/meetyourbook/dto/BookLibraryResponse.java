package com.meetyourbook.dto;

import com.meetyourbook.entity.BookLibrary;
import com.meetyourbook.entity.Library;
import lombok.Builder;

@Builder
public record BookLibraryResponse(Long id, String libraryName, String bookLibraryUrl) {

    public static BookLibraryResponse formEntity(BookLibrary bookLibrary) {
        Library library = bookLibrary.getLibrary();
        return new BookLibraryResponse(library.getId(), library.getName(), bookLibrary.getUrl());
    }

}
