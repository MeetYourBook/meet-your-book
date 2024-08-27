package com.meetyourbook.repository;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookLibraryRelation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookLibraryJdbcRepository {

    private static final String sql =
        "INSERT INTO book_library (book_id, library_id, url) VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE url = VALUES(url)";

    private final JdbcTemplate jdbcTemplate;


    public void saveAll(List<BookInfo> bookInfos, List<BookLibraryRelation> bookLibraryPairs) {

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BookLibraryRelation bookLibraryPair = bookLibraryPairs.get(i);
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
    }

    public byte[] uuidToBytes(UUID uuid) {
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
