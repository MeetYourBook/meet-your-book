package com.meetyourbook.controller.admin;

import com.meetyourbook.dto.BookCreateRequest;
import com.meetyourbook.dto.BookUpdateRequest;
import com.meetyourbook.service.BookService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Void> createBook(@RequestBody @Valid BookCreateRequest request) {
        Long id = bookService.createBook(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId,
        @RequestBody @Valid BookUpdateRequest request) {
        bookService.updateBook(bookId, request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(bookId)
            .toUri();
        return ResponseEntity.noContent().location(location).build();
    }

}
