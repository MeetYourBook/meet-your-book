package com.meetyourbook.repository;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookUniqueKey;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookJdbcRepository {

    private static final String FIND_ID_BY_UNIQUE_KEY_SQL =
        "SELECT id FROM book WHERE title = :title AND author = :author "
            + "AND publisher = :publisher AND publish_date = :publishDate";

    private static final String INSERT_BOOK_SQL =
        "INSERT INTO book (id, title, author, publisher, publish_date, provider, image_url) "
            + "VALUES (:id, :title, :author, :publisher, :publishDate, :provider, :imageUrl)";

    private final JdbcClient jdbcClient;

    public Optional<UUID> findIdByUniqueKey(BookUniqueKey bookUniqueKey) {

        return jdbcClient.sql(FIND_ID_BY_UNIQUE_KEY_SQL)
            .param("title", bookUniqueKey.title())
            .param("author", bookUniqueKey.author())
            .param("publisher", bookUniqueKey.publisher())
            .param("publishDate", bookUniqueKey.publishDate())
            .query((rs, rowNum) -> bytesToUuid(rs.getBytes("id")))
            .optional();
    }

    public UUID saveBook(BookInfo bookInfo) {
        UUID id = UUID.randomUUID();

        int rowsAffected = jdbcClient.sql(INSERT_BOOK_SQL)
            .param("id", uuidToBytes(id))
            .param("title", bookInfo.title())
            .param("author", bookInfo.author())
            .param("publisher", bookInfo.publisher())
            .param("publishDate", bookInfo.publishDate())
            .param("provider", bookInfo.provider())
            .param("imageUrl", bookInfo.imageUrl())
            .update();

        if (rowsAffected != 1) {
            log.error("BookInfo 저장 실패. BookInfo: {}", bookInfo);
            throw new DataIntegrityViolationException("Book 저장에 실패했습니다. 영향받은 행: " + rowsAffected);
        }

        log.debug("Book 저장 성공. ID: {}", id);
        return id;
    }

    private byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    private UUID bytesToUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

}
