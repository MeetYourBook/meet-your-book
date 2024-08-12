package com.meetyourbook.service;

import com.meetyourbook.dto.BookCreateRequest;
import com.meetyourbook.dto.BookPageResponse;
import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.BookUpdateRequest;
import com.meetyourbook.entity.Book;
import com.meetyourbook.exception.ResourceNotFoundException;
import com.meetyourbook.repository.BookRepository;
import com.meetyourbook.spec.BookSpecs;
import com.meetyourbook.spec.SpecBuilder;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookPageResponse searchBooks(BookSearchRequest request, Pageable pageable) {
        Specification<Book> spec = createBookSpec(request);
        Page<Book> bookPage = bookRepository.findAll(spec, pageable);
        return BookPageResponse.of(bookPage);
    }

    @Transactional
    public UUID createBook(BookCreateRequest request) {
        Book book = request.toEntity();
        bookRepository.save(book);
        return book.getId();
    }

    @Transactional
    public void deleteBook(UUID id) {
        Book book = findBook(id);
        bookRepository.delete(book);
    }

    @Transactional
    public void updateBook(UUID bookId, BookUpdateRequest request) {
        Book book = findBook(bookId);
        book.update(request);
    }

    private Book findBook(UUID id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
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
