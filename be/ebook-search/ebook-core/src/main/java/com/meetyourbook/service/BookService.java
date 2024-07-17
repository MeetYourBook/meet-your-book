package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public void saveAll(List<BookInfo> bookInfos, Library library) {
        List<Book> books = bookInfos.stream()
            .map(this::findOrCreateBook)
            .peek(book -> book.add(library))
            .toList();

        bookRepository.saveAll(books);
    }

    public Book findOrCreateBook(BookInfo bookInfo) {
        Optional<Book> optionalBook = bookRepository.findByTitleAndAuthorAndPublisherAndPublishDate(
            bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher(),
            bookInfo.getPublishDate());

        return optionalBook.orElseGet(bookInfo::toEntity);
    }

}
