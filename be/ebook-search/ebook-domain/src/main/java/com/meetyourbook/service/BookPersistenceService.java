package com.meetyourbook.service;

import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import com.meetyourbook.repository.jdbc.BookJdbcRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookPersistenceService {

    private final BookJdbcRepository bookJdbcRepository;
    private final Map<BookUniqueKey, Long> bookCache = new HashMap<>();

    public Map<BookRecord, Long> processBooks(List<BookRecord> bookRecords) {
        Map<BookRecord, Long> result = new HashMap<>();
        List<BookRecord> notInCache = findBooksNotInCache(bookRecords, result);
        log.debug("캐시에 없는 책: {}", notInCache.size());

        if (!notInCache.isEmpty()) {
            List<BookRecord> notInDb = findBooksNotInDb(notInCache, result);
            log.debug("DB에 없는 책: {}", notInDb.size());

            if (!notInDb.isEmpty()) {
                deduplicateAndSaveNewBooks(notInDb, result);
            }
        }
        log.debug("총 {}개의 책을 처리했습니다.", result.size());
        return result;
    }

    private List<BookRecord> findBooksNotInCache(List<BookRecord> bookRecords,
        Map<BookRecord, Long> result) {
        return bookRecords.stream()
            .filter(record -> {
                BookUniqueKey key = BookUniqueKey.from(record);
                Long cachedId = bookCache.get(key);
                if (cachedId != null) {
                    result.put(record, cachedId);
                    return false;
                }
                return true;
            })
            .toList();
    }

    private List<BookRecord> findBooksNotInDb(List<BookRecord> notInCache,
        Map<BookRecord, Long> result) {
        Map<BookUniqueKey, Long> existingIds = bookJdbcRepository.findIdsByUniqueKeys(
            notInCache.stream()
                .map(BookUniqueKey::from)
                .toList()
        );

        return notInCache.stream()
            .filter(record -> {
                BookUniqueKey key = BookUniqueKey.from(record);
                Long id = existingIds.get(key);
                if (id != null) {
                    result.put(record, id);
                    bookCache.put(key, id);
                    return false;
                }
                return true;
            })
            .toList();
    }

    private void deduplicateAndSaveNewBooks(List<BookRecord> notInDb,
        Map<BookRecord, Long> result) {
        Map<BookUniqueKey, BookRecord> uniqueRecordsMap = new HashMap<>();
        List<BookRecord> recordsToSave = new ArrayList<>();
        List<BookRecord> recordsNotToSave = new ArrayList<>();

        for (BookRecord record : notInDb) {
            BookUniqueKey key = BookUniqueKey.from(record);
            if (uniqueRecordsMap.putIfAbsent(key, record) == null) {
                recordsToSave.add(record);
            } else {
                recordsNotToSave.add(record);
            }
        }

        saveNewBooks(recordsToSave, result);
        log.debug("저장된 책: {}", recordsToSave.size());

        deduplicateBooks(recordsNotToSave, result);
        log.debug("중복된 책: {}", recordsNotToSave.size());
    }

    private void saveNewBooks(List<BookRecord> recordsToSave, Map<BookRecord, Long> result) {
        if (!recordsToSave.isEmpty()) {
            List<Long> savedIds = bookJdbcRepository.saveBooks(recordsToSave);
            for (int i = 0; i < recordsToSave.size(); i++) {
                BookRecord record = recordsToSave.get(i);
                Long id = savedIds.get(i);
                result.put(record, id);
                bookCache.put(BookUniqueKey.from(record), id);
            }
        }
    }

    private void deduplicateBooks(List<BookRecord> recordsNotToSave,
        Map<BookRecord, Long> result) {
        if (!recordsNotToSave.isEmpty()) {
            Map<BookUniqueKey, Long> duplicateIds = bookJdbcRepository.findIdsByUniqueKeys(
                recordsNotToSave.stream()
                    .map(BookUniqueKey::from)
                    .toList());

            for (BookRecord record : recordsNotToSave) {
                BookUniqueKey key = BookUniqueKey.from(record);
                Long id = duplicateIds.get(key);
                result.put(record, id);
                bookCache.put(key, id);
            }
        }
    }
}
