package com.meetyourbook.controller;

import com.meetyourbook.dto.BookPageResponse;
import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public BookPageResponse searchBooks(BookSearchRequest request) {
        return bookService.searchBooks(request);
    }

}
