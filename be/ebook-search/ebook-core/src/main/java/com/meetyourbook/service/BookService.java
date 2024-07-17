package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void saveAll(List<Book> books) {
        bookRepository.saveAll(books);
    }

    @Transactional
    public Book findOrCreateBook(BookInfo bookInfo) {
        Optional<Book> optionalBook = bookRepository.findByTitleAndAuthorAndPublisherAndPublishDate(
            bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher(),
            bookInfo.getPublishDate());

        return optionalBook.orElseGet(bookInfo::toEntity);
    }

}
