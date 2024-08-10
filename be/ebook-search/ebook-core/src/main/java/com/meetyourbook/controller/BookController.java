package com.meetyourbook.controller;

import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.SimpleBookResponse;
import com.meetyourbook.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<SimpleBookResponse> searchBooks(BookSearchRequest request, Pageable pageable) {
        return bookService.searchBooks(request, pageable);
    }

}
