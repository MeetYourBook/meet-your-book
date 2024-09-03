package com.meetyourbook.service;

import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import com.meetyourbook.repository.BookJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCacheService {

    private final BookJdbcRepository bookJdbcRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Cacheable(value = "bookCache", key = "#bookUniqueKey")
    public Long getBookIdFromCache(BookUniqueKey bookUniqueKey, BookRecord bookRecord) {
        return bookJdbcRepository.findIdByUniqueKey(bookUniqueKey)
            .orElseGet(() -> bookJdbcRepository.saveBook(bookRecord));
    }

}
