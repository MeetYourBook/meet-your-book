package com.meetyourbook.controller;

import com.meetyourbook.dto.BookCreateRequest;
import com.meetyourbook.dto.BookPageResponse;
import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.BookUpdateRequest;
import com.meetyourbook.service.BookService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public BookPageResponse searchBooks(BookSearchRequest request, Pageable pageable) {
        return bookService.searchBooks(request, pageable);
    }

    @PostMapping
    public ResponseEntity<Void> createBook(@RequestBody @Valid BookCreateRequest request) {
        UUID id = bookService.createBook(request);
        URI location = URI.create("/api/books/" + id);
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable UUID bookId,
        @RequestBody @Valid BookUpdateRequest request) {
        bookService.updateBook(bookId, request);
        return ResponseEntity.noContent().location(URI.create("/api/books/" + bookId)).build();
    }
}
