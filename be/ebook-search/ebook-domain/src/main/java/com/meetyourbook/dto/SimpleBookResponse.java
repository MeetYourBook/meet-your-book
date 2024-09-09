package com.meetyourbook.dto;

import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record SimpleBookResponse(Long id, String title, String author, String publisher,
                                 LocalDate publishDate,
                                 String imageUrl, List<BookLibraryResponse> libraryResponses) {

    public static SimpleBookResponse fromEntity(Book book) {
        List<BookLibraryResponse> bookLibraryResponses = book.getBookLibraries().stream()
            .map(BookLibraryResponse::formEntity).toList();

        return new SimpleBookResponse(book.getId(), book.getTitle(), book.getAuthor(),
            book.getPublisher(), book.getPublishDate(), book.getImageUrl(), bookLibraryResponses);
    }

}
