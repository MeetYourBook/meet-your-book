package com.meetyourbook.service;

import com.meetyourbook.dto.BookCreateRequest;
import com.meetyourbook.dto.BookCreationInfo;
import com.meetyourbook.dto.BookPageResponse;
import com.meetyourbook.dto.BookSearchInfo;
import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.BookUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookDomainService bookDomainService;

    public BookPageResponse searchBooks(BookSearchRequest request) {
        BookSearchInfo bookSearchInfo = request.toBookSearchInfo();
        return bookDomainService.searchBooks(bookSearchInfo);
    }

    public Long createBook(BookCreateRequest request) {
        BookCreationInfo bookCreationInfo = request.toBookCreation();
        return bookDomainService.createBook(bookCreationInfo);
    }

    public void deleteBook(Long id) {
        bookDomainService.deleteBook(id);
    }

    public void updateBook(Long bookId, BookUpdateRequest request) {
        bookDomainService.updateBook(bookId, request.toBookUpdateInfo());
    }

}
