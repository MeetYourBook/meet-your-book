package com.meetyourbook.service;

import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import com.meetyourbook.repository.jdbc.BookJdbcRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCacheService {

    private final BookJdbcRepository bookJdbcRepository;
    private final Map<BookUniqueKey, Long> bookCache = new HashMap<>();

    public Map<BookRecord, Long> processBook(List<BookRecord> bookRecords) {
        Map<BookRecord, Long> result = new HashMap<>();
        List<BookRecord> notInCache = new ArrayList<>();

        // 캐시에 있는지 확인
        for (BookRecord bookRecord : bookRecords) {
            BookUniqueKey bookUniqueKey = BookUniqueKey.from(bookRecord);
            Long cacheId = bookCache.get(bookUniqueKey);
            if (bookCache.containsKey(bookUniqueKey)) {
                result.put(bookRecord, cacheId);
            } else {
                notInCache.add(bookRecord);
            }
        }

        List<BookRecord> notInDb = new ArrayList<>();
        // DB에 있는지 확인
        if (!notInCache.isEmpty()) {
            Map<BookUniqueKey, Long> existingIds = bookJdbcRepository.findIdsByUniqueKeys(
                notInCache.stream().map(BookUniqueKey::from).collect(
                    Collectors.toList()));

            for (BookRecord record : notInCache) {
                BookUniqueKey key = BookUniqueKey.from(record);
                Long id = existingIds.get(key);
                if (existingIds.containsKey(key)) {
                    result.put(record, id);
                    bookCache.put(key, id);
                } else {
                    notInDb.add(record);
                }
            }
        }

        if (!notInDb.isEmpty()) {
            // 중복확인
            List<BookRecord> recordsToSave = new ArrayList<>();
            List<BookRecord> recordsNotToSave = new ArrayList<>();
            Map<BookUniqueKey, BookRecord> uniqueRecordsMap = new HashMap<>();
            for (BookRecord record : notInDb) {
                BookUniqueKey key = BookUniqueKey.from(record);
                if (uniqueRecordsMap.containsKey(key)) {
                    recordsNotToSave.add(record);
                } else {
                    uniqueRecordsMap.put(key, record);
                    recordsToSave.add(record);
                }
            }

            if (!recordsToSave.isEmpty()) {
                List<Long> savedIds = bookJdbcRepository.saveBooks(recordsToSave);
                for (int i = 0; i < recordsToSave.size(); i++) {
                    BookRecord record = recordsToSave.get(i);
                    Long id = savedIds.get(i);
                    result.put(record, id);
                    bookCache.put(BookUniqueKey.from(record), id);
                }
            }

            if (!recordsNotToSave.isEmpty()) {
                bookJdbcRepository.findIdsByUniqueKeys(recordsNotToSave
                    .stream().map(BookUniqueKey::from).collect(Collectors.toList()));
            }
            if (!recordsNotToSave.isEmpty()) {
                for (BookRecord record : recordsNotToSave) {
                    BookUniqueKey key = BookUniqueKey.from(record);
                    Long id = bookCache.get(key);
                    result.put(record, id);
                }
            }
        }
        return result;
    }


}
