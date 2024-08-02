package com.meetyourbook.dto;

import com.meetyourbook.entity.BookLibrary;
import com.meetyourbook.entity.Library;

public record BookLibraryResponse(Long id, String LibraryName, String BookLibraryUrl) {

    public static BookLibraryResponse formEntity(BookLibrary bookLibrary) {
        Library library = bookLibrary.getLibrary();
        return new BookLibraryResponse(library.getId(), library.getName(), bookLibrary.getUrl());
    }

}
