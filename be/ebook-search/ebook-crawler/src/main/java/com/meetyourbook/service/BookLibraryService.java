package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookLibraryRelation;
import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import com.meetyourbook.repository.BookLibraryJdbcRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookLibraryService {

    private final BookCacheService bookCacheService;
    private final LibraryCacheService libraryCacheService;
    private final BookLibraryJdbcRepository bookLibraryJdbcRepository;

    @Transactional
    public void saveBookInfos(List<BookInfo> bookInfos) {
        List<BookRecord> bookRecords = bookInfos.stream()
            .map(BookRecord::from)
            .toList();
        List<BookLibraryRelation> bookLibraries = createBookLibraryRelations(bookRecords);
        bookLibraryJdbcRepository.saveAll(bookLibraries);
    }

    private List<BookLibraryRelation> createBookLibraryRelations(List<BookRecord> bookRecords) {
        return bookRecords.stream()
            .map(this::createBookLibraryRelation)
            .toList();
    }

    private BookLibraryRelation createBookLibraryRelation(BookRecord bookRecord) {
        Long bookId = bookCacheService.getBookIdFromCache(BookUniqueKey.from(bookRecord), bookRecord);
        Long libraryId = libraryCacheService.getLibraryFromCache(bookRecord.baseUrl());

        return new BookLibraryRelation(bookId, libraryId, bookRecord.bookUrl());
    }




}