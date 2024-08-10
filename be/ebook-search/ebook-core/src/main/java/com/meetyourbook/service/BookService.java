package com.meetyourbook.service;

import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.SimpleBookResponse;
import com.meetyourbook.entity.Book;
import com.meetyourbook.repository.BookRepository;
import com.meetyourbook.spec.BookSpecs;
import com.meetyourbook.spec.SpecBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public List<SimpleBookResponse> searchBooks(BookSearchRequest request) {

        Specification<Book> spec = createBookSpec(request);

        List<Book> books = bookRepository.findAll(spec);

        return books.stream()
            .map(SimpleBookResponse::fromEntity)
            .toList();
    }

    private Specification<Book> createBookSpec(BookSearchRequest request) {
        return SpecBuilder.builder(Book.class)
            .ifNotNullOr(request.title(), BookSpecs::titleContains)
            .ifNotNullOr(request.author(), BookSpecs::authorContains)
            .ifNotNullOr(request.publisher(), BookSpecs::publisherContains)
            .ifNotNullAnd(request.libraries(), BookSpecs::inLibraries)
            .toSpec();
    }

}
