package com.meetyourbook.service;

import com.meetyourbook.entity.Book;
import com.meetyourbook.repository.BookRepository;
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
