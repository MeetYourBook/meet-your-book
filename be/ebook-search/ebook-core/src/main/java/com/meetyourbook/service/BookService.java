package com.meetyourbook.service;

import com.ebooksearch.common.entity.Book;
import com.ebooksearch.common.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
