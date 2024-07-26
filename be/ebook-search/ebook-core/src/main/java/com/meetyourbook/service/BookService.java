package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.BookRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;



    @Transactional
    public void saveAll(List<BookInfo> bookInfos) {

        List<Book> books = new ArrayList<>();
        for (BookInfo bookInfo : bookInfos) {
            Book book = findOrCreateBook(bookInfo);
            Library library = libraryService.findByBaseUrl(bookInfo.getBaseUrl());
            book.addLibrary(library);
            books.add(book);
        }

        bookRepository.saveAll(books);
    }

    private Book findOrCreateBook(BookInfo bookInfo) {
        Optional<Book> optionalBook = bookRepository.findFirstByTitleAndAuthorAndPublisherAndPublishDate(
            bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher(),
            bookInfo.getPublishDate());

        return optionalBook.orElseGet(bookInfo::toEntity);
    }

}
