package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.BookLibrary;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.BookLibraryRepository;
import com.meetyourbook.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookLibraryService {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;
    private final BookLibraryRepository bookLibraryRepository;
    private final Map<String, Library> libraryCache = new ConcurrentHashMap<>();

    @Transactional
    public void saveAll(List<BookInfo> bookInfos) {
        log.info("Saving {} books", bookInfos.size());

        List<BookLibrary> bookLibraries = new ArrayList<>();
        for (BookInfo bookInfo : bookInfos) {
            Book book = getBook(bookInfo);
            Library library = getLibrary(bookInfo);
            BookLibrary bookLibrary = BookLibrary.createBookLibrary(book, library);
            bookLibraries.add(bookLibrary);
        }
        bookLibraryRepository.saveAll(bookLibraries);
    }

    private Book getBook(BookInfo bookInfo) {
        return findBookByBookInfo(bookInfo)
            .orElseGet(() -> bookRepository.save(bookInfo.toEntity()));
    }

    private Library getLibrary(BookInfo bookInfo) {
        return libraryCache.computeIfAbsent(bookInfo.getBaseUrl(),
            libraryService::findByBaseUrl);
    }

    private Optional<Book> findBookByBookInfo(BookInfo bookInfo) {
        return bookRepository.findFirstByTitleAndAuthorAndPublisherAndPublishDate(
            bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher(),
            bookInfo.getPublishDate());
    }

}
