package com.meetyourbook.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.dto.BookRecord;
import com.meetyourbook.dto.BookUniqueKey;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;

@JdbcTest
class BookJdbcRepositoryTest {

    @Autowired
    private JdbcClient jdbcClient;

    private BookJdbcRepository bookJdbcRepository;

    @BeforeEach
    void setUp() {
        bookJdbcRepository = new BookJdbcRepository(jdbcClient);
    }

    @Test
    @DisplayName("존재하지 않는 책을 검색하면 Optional.empty를 반환한다")
    void findIdByUniqueKeyWhenBookDoesNotExist() {
        // Given
        BookUniqueKey bookUniqueKey = new BookUniqueKey("제목", "저자", "출판사", LocalDate.now());

        // When
        Optional<UUID> optionalUUID = bookJdbcRepository.findIdByUniqueKey(bookUniqueKey);

        // Then
        assertThat(optionalUUID).isEmpty();
    }

    @Test
    @DisplayName("존재하는 책을 검색하면 해당 책의 UUID를 반환한다")
    void findIdByUniqueKeyWhenBookExists() {
        // Given
        BookInfo bookInfo = createSampleBookInfo();
        BookRecord bookRecord = BookRecord.from(bookInfo);
        UUID savedId = bookJdbcRepository.saveBook(bookRecord);
        BookUniqueKey bookUniqueKey = BookUniqueKey.from(bookRecord);

        // When
        Optional<UUID> optionalUUID = bookJdbcRepository.findIdByUniqueKey(bookUniqueKey);

        // Then
        assertThat(optionalUUID)
            .isPresent()
            .get()
            .isEqualTo(savedId);
    }

    @Test
    @DisplayName("새로운 책 정보를 저장하면 UUID를 반환한다")
    void saveBookSuccessfully() {
        // Given
        BookInfo bookInfo = createSampleBookInfo();
        BookRecord bookRecord = BookRecord.from(bookInfo);

        // When
        UUID savedId = bookJdbcRepository.saveBook(bookRecord);

        // Then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("책 저장에 실패하면 RuntimeException 을 던진다")
    void saveBookFailure() {
        // Given
        BookInfo invalidBookInfo = BookInfo.builder()
            .title(null)
            .author("저자")
            .publisher("출판사")
            .publishDate(LocalDate.now())
            .bookUrl("www.testurl.com")
            .imageUrl("www.testurl.com")
            .build();
        BookRecord invalidBookRecord = BookRecord.from(invalidBookInfo);

        // When & Then
        assertThatThrownBy(() -> bookJdbcRepository.saveBook(invalidBookRecord))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    private BookInfo createSampleBookInfo() {
        return BookInfo.builder()
            .title("제목")
            .author("저자")
            .publisher("출판사")
            .publishDate(LocalDate.of(2000, 5, 11))
            .bookUrl("www.testurl.com")
            .imageUrl("www.testurl.com")
            .build();
    }

}