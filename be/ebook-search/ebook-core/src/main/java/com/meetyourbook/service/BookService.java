package com.meetyourbook.service;

import com.meetyourbook.repository.BookRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void findBook(UUID id) {
        bookRepository.findById(id).orElseThrow();
    }

}
