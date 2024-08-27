package com.meetyourbook.service;

import com.meetyourbook.common.QueryCountInspector;
import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookLibraryRelation;
import com.meetyourbook.dto.BookUniqueKey;
import com.meetyourbook.entity.Book;
import com.meetyourbook.repository.BookLibraryJdbcRepository;
import com.meetyourbook.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookLibraryService {

    private final LibraryDomainService libraryDomainService;
    private final BookLibraryJdbcRepository bookLibraryJdbcRepository;
    private final BookRepository bookRepository;
    private final Map<BookUniqueKey, UUID> bookCache = new ConcurrentHashMap<>();
    private final Map<String, Long> libraryCache = new ConcurrentHashMap<>();
    private final List<BookLibraryRelation> bookLibraryRelations = new ArrayList<>();
    private final QueryCountInspector queryCountInspector;

    @Transactional
    public void saveAll(List<BookInfo> bookInfos) {
        log.info("bookInfos size: {}", bookInfos.size());
        saveBooks(bookInfos);
        saveBookLibraries(bookInfos);
        bookLibraryRelations.clear();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void saveBooks(List<BookInfo> bookInfos) {
        queryCountInspector.startCounter();

        findBooksByBookInfos(bookInfos);
        for (BookInfo bookInfo : bookInfos) {
            addBookLibraryRelation(bookInfo);
        }

        log.info("query count: {}", queryCountInspector.getQueryCount().getCount());
        queryCountInspector.clearCounter();
    }

    @Transactional
    protected void saveBookLibraries(List<BookInfo> bookInfos) {
        bookLibraryJdbcRepository.saveAll(bookInfos, bookLibraryRelations);
    }

    private void addBookLibraryRelation(BookInfo bookInfo) {
        UUID bookId = getBookIdFromCache(bookInfo);
        Long libraryId = getLibraryFromCache(bookInfo);
        bookLibraryRelations.add(
            new BookLibraryRelation(bookId, libraryId, bookInfo.bookUrl()));
    }

    private void findBooksByBookInfos(List<BookInfo> bookInfos) {
        for (BookInfo bookInfo : bookInfos) {
            BookUniqueKey bookCacheKey = BookUniqueKey.from(bookInfo);

            if (bookCache.containsKey(bookCacheKey)) {
                continue;
            }
            bookRepository.findBooksByBookInfo(bookInfo)
                .ifPresent(book -> bookCache.put(bookCacheKey, book.getId()));
        }
    }

    private UUID getBookIdFromCache(BookInfo bookInfo) {
        BookUniqueKey bookUniqueKey = BookUniqueKey.from(bookInfo);
        return bookCache.computeIfAbsent(bookUniqueKey,
            key -> {
                Book savedBook = bookRepository.save(bookInfo.toEntity());
                return savedBook.getId();
            });
    }

    private Long getLibraryFromCache(BookInfo bookInfo) {
        return libraryCache.computeIfAbsent(bookInfo.baseUrl(),
            baseurl -> libraryDomainService.findByBaseUrl(baseurl).getId());
    }

}
