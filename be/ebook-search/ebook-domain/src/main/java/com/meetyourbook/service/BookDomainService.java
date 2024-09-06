package com.meetyourbook.service;

import com.meetyourbook.dto.BookCreationInfo;
import com.meetyourbook.dto.BookPageResponse;
import com.meetyourbook.dto.BookSearchInfo;
import com.meetyourbook.dto.BookUpdateInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.exception.ResourceNotFoundException;
import com.meetyourbook.repository.jpa.BookRepository;
import com.meetyourbook.spec.BookSpecs;
import com.meetyourbook.spec.SpecBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookDomainService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookPageResponse searchBooks(BookSearchInfo bookSearchInfo) {
        Specification<Book> spec = createBookSpec(bookSearchInfo);
        Page<Book> bookPage = bookRepository.findAll(spec, bookSearchInfo.toPageable());
        return BookPageResponse.of(bookPage);
    }

    @Transactional
    public Long createBook(BookCreationInfo bookCreationInfo) {
        Book book = bookCreationInfo.toEntity();
        bookRepository.save(book);
        return book.getId();
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBook(id);
        bookRepository.delete(book);
    }

    @Transactional
    public void updateBook(Long bookId, BookUpdateInfo bookUpdateInfo) {
        Book book = findBook(bookId);
        book.update(bookUpdateInfo);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private Specification<Book> createBookSpec(BookSearchInfo bookSearchInfo) {
        return SpecBuilder.builder(Book.class)
            .ifNotNullOr(bookSearchInfo.title(), BookSpecs::titleContains)
            .ifNotNullOr(bookSearchInfo.author(), BookSpecs::authorContains)
            .ifNotNullOr(bookSearchInfo.publisher(), BookSpecs::publisherContains)
            .ifNotNullAnd(bookSearchInfo.libraries(), BookSpecs::inLibraries)
            .toSpec();
    }
}
