package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
import java.util.List;
import java.util.UUID;

public record SimpleBookResponse(UUID id, String title, String author, String provider,
                                 String publisher, String imageUrl,
                                 List<BookLibraryResponse> libraryResponses) {

    public static SimpleBookResponse fromEntity(Book book) {
        List<BookLibraryResponse> bookLibraryResponses = book.getBookLibraries().stream()
            .map(BookLibraryResponse::formEntity).toList();

        return new SimpleBookResponse(book.getId(), book.getTitle(), book.getAuthor(),
            book.getProvider(), book.getPublisher(), book.getImageUrl(), bookLibraryResponses);
    }

}
