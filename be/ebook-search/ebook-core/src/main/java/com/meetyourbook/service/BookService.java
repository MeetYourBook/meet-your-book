package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.BookRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final LibraryService libraryService;
    private final BookRepository bookRepository;

    @Transactional
    public void saveAll(List<BookInfo> bookInfos) {
        log.info("Saving {} books", bookInfos.size());
        Map<String, Library> libraryCache = new HashMap<>();

        List<Book> booksToSave = bookInfos.stream()
            .map(bookInfo -> processBookInfo(bookInfo, libraryCache)).toList();

        bookRepository.saveAll(booksToSave);
    }

    private Book processBookInfo(BookInfo bookInfo, Map<String, Library> libraryCache) {
        Library library = libraryCache.computeIfAbsent(bookInfo.getBaseUrl(),
            libraryService::findByBaseUrl);

        Book book = findBookByBookInfo(bookInfo).orElseGet(bookInfo::toEntity);
        book.addLibrary(library);
        return book;
    }

    private Optional<Book> findBookByBookInfo(BookInfo bookInfo) {
        return bookRepository.findFirstByTitleAndAuthorAndPublisherAndPublishDate(
            bookInfo.getTitle(), bookInfo.getAuthor(), bookInfo.getPublisher(),
            bookInfo.getPublishDate());
    }

}
