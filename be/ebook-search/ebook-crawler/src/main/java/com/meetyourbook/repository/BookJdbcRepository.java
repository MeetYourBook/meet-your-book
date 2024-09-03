package com.meetyourbook.repository;

import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookJdbcRepository {

    private static final String FIND_ID_BY_UNIQUE_KEY_SQL =
        "SELECT id FROM book WHERE title = :title AND author = :author "
            + "AND publisher = :publisher AND publish_date = :publishDate";

    private static final String INSERT_BOOK_SQL =
        "INSERT INTO book (title, author, publisher, publish_date, image_url, created_at, updated_at) "
            + "VALUES (:title, :author, :publisher, :publishDate, :imageUrl, :now, :now)";

    private final JdbcClient jdbcClient;

    public Optional<Long> findIdByUniqueKey(BookUniqueKey bookUniqueKey) {
        return jdbcClient.sql(FIND_ID_BY_UNIQUE_KEY_SQL)
            .param("title", bookUniqueKey.title())
            .param("author", bookUniqueKey.author())
            .param("publisher", bookUniqueKey.publisher())
            .param("publishDate", bookUniqueKey.publishDate())
            .query((rs, rowNum) -> rs.getLong("id"))
            .optional();
    }

    public Long saveBook(BookRecord bookRecord) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(INSERT_BOOK_SQL)
            .param("title", bookRecord.title())
            .param("author", bookRecord.author())
            .param("publisher", bookRecord.publisher())
            .param("publishDate", bookRecord.publishDate())
            .param("imageUrl", bookRecord.imageUrl())
            .param("now", bookRecord.dateTime())
            .update(keyHolder);

        if (rowsAffected != 1) {
            log.error("BookInfo 저장 실패. BookInfo: {}", bookRecord);
            throw new DataIntegrityViolationException("Book 저장에 실패했습니다. 영향받은 행: " + rowsAffected);
        }

        Long id = keyHolder.getKey().longValue();
        log.debug("Book 저장 성공. ID: {}", id);
        return id;
    }

}
