package com.meetyourbook.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.dto.BookLibraryRelation;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookLibraryJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BookLibraryJdbcRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BookLibraryJdbcRepository(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("BookLibrary 를 저장할 수 있다.")
    void testSaveAll() {
        // Given
        List<BookLibraryRelation> relations = List.of(
            new BookLibraryRelation(UUID.randomUUID(), 1L, "http://example.com/book1"),
            new BookLibraryRelation(UUID.randomUUID(), 2L, "http://example.com/book2")
        );

        // When
        repository.saveAll(relations);

        // Then
        int count = JdbcTestUtils.countRowsInTable(jdbcTemplate, "book_library");
        assertThat(count).isEqualTo(relations.size());
    }

    @Test
    @DisplayName("BookLibrary 저장 시 중복된 데이터는 업데이트되어야 한다")
    void shouldUpdateExistingBookLibraryWhenSaving() {
        // Given
        UUID bookId = UUID.randomUUID();
        long libraryId = 1L;

        List<BookLibraryRelation> oldRelations = List.of(
            new BookLibraryRelation(bookId, libraryId, "http://example.com/book1")
        );
        List<BookLibraryRelation> newRelation = List.of(
            new BookLibraryRelation(bookId, libraryId, "http://newUrl.com")
        );
        repository.saveAll(oldRelations);

        // When
        repository.saveAll(newRelation);

        // Then
        String sql = "SELECT url FROM book_library WHERE book_id = ? AND library_id = ?";
        String url = jdbcTemplate.queryForObject(sql, String.class, uuidToBytes(bookId), libraryId);
        assertThat(url).isEqualTo("http://newUrl.com");
    }

    private byte[] uuidToBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }


}