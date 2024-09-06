package com.meetyourbook.repository.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.common.JdbcRepositoryTest;
import com.meetyourbook.dto.BookLibraryRelation;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@JdbcRepositoryTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookLibraryJdbcRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BookLibraryJdbcRepository repository;

    @BeforeEach
    void setUp() {
        disableForeignKey();
        repository = new BookLibraryJdbcRepository(namedParameterJdbcTemplate);
    }

    private void disableForeignKey() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
    }

    @Test
    @DisplayName("BookLibrary 를 저장할 수 있다.")
    void testSaveAll() {
        // Given
        Long id = 1L;
        List<BookLibraryRelation> relations = List.of(
            new BookLibraryRelation(id, 1L, "http://example.com/book1"),
            new BookLibraryRelation(id, 2L, "http://example.com/book2")
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
        Long bookId = 1L;
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
        String url = jdbcTemplate.queryForObject(sql, String.class, bookId, libraryId);
        assertThat(url).isEqualTo("http://newUrl.com");
    }

    @Test
    @DisplayName("BookLibrary 저장 시 생성시간, 수정시간을 저장한다")
    void createdAtUpdatedAt() {
        // Given
        Long bookId = 1L;
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
        String url = jdbcTemplate.queryForObject(sql, String.class, bookId, libraryId);
        assertThat(url).isEqualTo("http://newUrl.com");
    }

}