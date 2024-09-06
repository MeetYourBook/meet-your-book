package com.meetyourbook.repository.jdbc;

import com.meetyourbook.dto.BookLibraryRelation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookLibraryJdbcRepository {

    private static final String SAVE_BOOK_LIBRARY_SQL =
        "INSERT INTO book_library (book_id, library_id, url, created_at, updated_at) VALUES (:bookId, :libraryId, :url, :now, :now) "
            +
            "ON DUPLICATE KEY UPDATE url = VALUES(url), updated_at = :now";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void saveAll(List<BookLibraryRelation> bookLibraryPairs) {

        SqlParameterSource[] batchArgs = bookLibraryPairs.stream()
            .map(relation -> new MapSqlParameterSource()
                .addValue("bookId", relation.bookId())
                .addValue("libraryId", relation.libraryId())
                .addValue("url", relation.bookUrl())
                .addValue("now", relation.dateTime()))
            .toArray(SqlParameterSource[]::new);

        int[] updateCounts = namedParameterJdbcTemplate.batchUpdate(SAVE_BOOK_LIBRARY_SQL,
            batchArgs);

        log.info("저장된 book-library 관계 수: {}", updateCounts.length);
    }

}