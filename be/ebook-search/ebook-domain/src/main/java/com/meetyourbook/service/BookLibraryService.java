package com.meetyourbook.service;

import com.meetyourbook.common.QueryCountInspector;
import com.meetyourbook.dto.BookCache;
import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookLibraryPair;
import com.meetyourbook.repository.BookRepository;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookLibraryService {

    private static final String sql =
        "INSERT INTO book_library (book_id, library_id, url) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE url = VALUES(url)";

    private final LibraryDomainService libraryDomainService;
    private final BookRepository bookRepository;
    private final Map<BookCache, UUID> bookCache = new ConcurrentHashMap<>();
    private final Map<String, Long> libraryCache = new ConcurrentHashMap<>();
    private final QueryCountInspector queryCountInspector;
    private final JdbcTemplate jdbcTemplate;
    private final List<BookLibraryPair> bookLibraryPairs = new ArrayList<>();

    @Transactional
    public void saveAll(List<BookInfo> bookInfos) {
        log.info("bookInfos size: {}", bookInfos.size());
        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    BookLibraryPair bookLibraryPair = bookLibraryPairs.get(i);
                    ps.setObject(1, uuidToBytes(bookLibraryPair.bookId()));
                    ps.setLong(2, bookLibraryPair.libraryId());
                    ps.setString(3, bookLibraryPair.bookUrl());
                }

                @Override
                public int getBatchSize() {
                    log.info("batch size: {}", bookInfos.size());
                    return bookInfos.size();
                }
            });
        } catch (Exception e) {
            log.error("error", e);
        }
        bookLibraryPairs.clear();

    }

    @Transactional
    public void saveBooks(List<BookInfo> bookInfos) {
        queryCountInspector.startCounter();
        findBooksByBookInfos(bookInfos);
        try {
            for (BookInfo bookInfo : bookInfos) {
                BookCache bookCacheKey = new BookCache(bookInfo.title(), bookInfo.author(),
                    bookInfo.publisher(), bookInfo.publishDate());

                if (bookCache.containsKey(bookCacheKey)) {
                    UUID bookId = bookCache.get(bookCacheKey);
                    Long libraryId = getLibrary(bookInfo);
                    bookLibraryPairs.add(
                        new BookLibraryPair(bookId, libraryId, bookInfo.bookUrl()));
                } else {
                    UUID bookId = bookRepository.save(bookInfo.toEntity()).getId();
                    bookCache.put(bookCacheKey, bookId);
                    Long libraryId = getLibrary(bookInfo);
                    bookLibraryPairs.add(
                        new BookLibraryPair(bookId, libraryId, bookInfo.bookUrl()));
                }

            }
        } finally {
            log.info("query count: {}", queryCountInspector.getQueryCount().getCount());
        }
        queryCountInspector.clearCounter();
    }


    private Long getLibrary(BookInfo bookInfo) {
        Long libraryId = libraryCache.computeIfAbsent(bookInfo.baseUrl(),
            baseurl -> libraryDomainService.findByBaseUrl(baseurl).getId());
        return libraryId;
    }

    public void findBooksByBookInfos(List<BookInfo> bookInfos) {
        for (BookInfo bookInfo : bookInfos) {
            BookCache bookCacheKey = new BookCache(bookInfo.title(), bookInfo.author(),
                bookInfo.publisher(), bookInfo.publishDate());
            if (bookCache.containsKey(bookCacheKey)) {
                continue;
            }
            bookRepository.findBooksByBookInfo(bookInfo.title(), bookInfo.author(),
                    bookInfo.publisher(), bookInfo.publishDate())
                .ifPresent(book -> bookCache.put(bookCacheKey, book.getId()));
        }
    }

    public byte[] uuidToBytes(UUID uuid) {
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
