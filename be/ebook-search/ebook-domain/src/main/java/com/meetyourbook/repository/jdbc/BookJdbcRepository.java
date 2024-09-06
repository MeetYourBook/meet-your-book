package com.meetyourbook.repository.jdbc;

import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Long> findIdByUniqueKey(BookUniqueKey bookUniqueKey) {
        return jdbcClient.sql(FIND_ID_BY_UNIQUE_KEY_SQL)
            .param("title", bookUniqueKey.title())
            .param("author", bookUniqueKey.author())
            .param("publisher", bookUniqueKey.publisher())
            .param("publishDate", bookUniqueKey.publishDate())
            .query((rs, rowNum) -> rs.getLong("id"))
            .optional();
    }

    public Map<BookUniqueKey, Long> findIdsByUniqueKeys(List<BookUniqueKey> keys) {
        String sql = "SELECT id, title, author, publisher, publish_date FROM book WHERE (title, author, publisher, publish_date) IN (:keys)";

        return jdbcClient.sql(sql)
            .param("keys", keys.stream()
                .map(k -> new Object[]{k.title(), k.author(), k.publisher(), k.publishDate()})
                .collect(
                    Collectors.toList()))
            .query((rs, rowNum) -> new AbstractMap.SimpleEntry<>(
                new BookUniqueKey(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getDate("publish_date").toLocalDate()
                ),
                rs.getLong("id")
            ))
            .list()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
            .update(keyHolder, "id");

        if (rowsAffected != 1) {
            log.error("BookInfo 저장 실패. BookInfo: {}", bookRecord);
            throw new DataIntegrityViolationException("Book 저장에 실패했습니다. 영향받은 행: " + rowsAffected);
        }

        Long id = keyHolder.getKey().longValue();
        log.debug("Book 저장 성공. ID: {}", id);
        return id;
    }

    public List<Long> saveBooks(List<BookRecord> books) {
        String sql =
            "INSERT INTO book (title, author, publisher, publish_date, image_url, created_at, updated_at) "
                +
                "VALUES (:title, :author, :publisher, :publishDate, :imageUrl, :now, :now)";
        String selectSql = "SELECT LAST_INSERT_ID()";

        List<Long> generatedIds = new ArrayList<>();

        SqlParameterSource[] batchArgs = books.stream()
            .map(book -> new MapSqlParameterSource()
                .addValue("title", book.title())
                .addValue("author", book.author())
                .addValue("publisher", book.publisher())
                .addValue("publishDate", book.publishDate())
                .addValue("imageUrl", book.imageUrl())
                .addValue("now", book.dateTime()))
            .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, batchArgs);

        long lastId = namedParameterJdbcTemplate.queryForObject(selectSql,
            new MapSqlParameterSource(), Long.class);

        for (long i = 0; i < books.size(); i++) {
            generatedIds.add(lastId - books.size() + i + 1);
        }
        return generatedIds;
    }


}
