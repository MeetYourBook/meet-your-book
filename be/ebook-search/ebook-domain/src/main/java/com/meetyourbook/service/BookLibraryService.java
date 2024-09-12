package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookLibraryRelation;
import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.repository.jdbc.BookLibraryJdbcRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookLibraryService {

    private final BookPersistenceService bookPersistenceService;
    private final LibraryCacheService libraryCacheService;
    private final BookLibraryJdbcRepository bookLibraryJdbcRepository;

    @Transactional
    public void saveBookInfos(List<BookInfo> bookInfos) {
        List<BookRecord> bookRecords = bookInfos.stream()
            .map(BookRecord::from)
            .toList();
        Map<BookRecord, Long> processedBooks = bookPersistenceService.processBooks(bookRecords);
        List<BookLibraryRelation> bookLibraries = createBookLibraryRelations(processedBooks);
        bookLibraryJdbcRepository.saveAll(bookLibraries);
    }

    private List<BookLibraryRelation> createBookLibraryRelations(
        Map<BookRecord, Long> processedBooks) {
        return processedBooks.entrySet().stream()
            .map(this::createBookLibraryRelation)
            .toList();
    }

    private BookLibraryRelation createBookLibraryRelation(Map.Entry<BookRecord, Long> entry) {
        Long bookId = entry.getValue();
        Long libraryId = libraryCacheService.getLibraryFromCache(entry.getKey().baseUrl());

        return new BookLibraryRelation(bookId, libraryId, entry.getKey().bookUrl());
    }


}