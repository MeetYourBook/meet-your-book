package com.meetyourbook.service;

import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.SimpleBookResponse;
import com.meetyourbook.entity.Book;
import com.meetyourbook.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<SimpleBookResponse> searchBooks(BookSearchRequest request) {

        List<Book> books = switch (request.condition()) {
            case TITLE -> bookRepository.searchByTitle(request.query(), request.libraryIds());
            case AUTHOR -> bookRepository.searchByAuthor(request.query(), request.libraryIds());
            case PUBLISHER -> bookRepository.searchByPublisher(request.query(), request.libraryIds());
            case ALL -> bookRepository.searchByAll(request.query(), request.libraryIds());
        };

        return books.stream()
            .map(SimpleBookResponse::fromEntity)
            .toList();

    }


}
