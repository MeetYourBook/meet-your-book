package com.meetyourbook.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.common.JpaRepositoryTest;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.BookLibrary;
import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.EbookPlatform;
import com.meetyourbook.repository.jpa.BookLibraryRepository.BookLibraryCount;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@JpaRepositoryTest
class BookLibraryRepositoryTest {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    @Test
    @DisplayName("도서관별 보유한 책의 수를 GroupBy로 조회한다.")
    void getTotalBookCountByUsingGroupBy() {
        // Given
        Library library1 = Library.builder()
            .name("Library 1")
            .ebookPlatform(EbookPlatform.KYOBO)
            .build();

        libraryRepository.save(library1);

        Book book1 = Book.builder().title("Book 1").build();
        Book book2 = Book.builder().title("Book 2").build();

        // Book 엔티티를 먼저 저장
        bookRepository.saveAll(List.of(book1, book2));

        BookLibrary bookLibrary1 = BookLibrary.createBookLibrary(
            book1,
            library1,
            "https://example.com/book1"
        );
        BookLibrary bookLibrary2 = BookLibrary.createBookLibrary(
            book2,
            library1,
            "https://example.com/book2"
        );

        bookLibraryRepository.saveAll(List.of(bookLibrary1, bookLibrary2));

        // When
        List<BookLibraryCount> bookLibraryCount = bookLibraryRepository.getBookLibraryCount();
        Map<Long, Integer> result = bookLibraryCount.stream()
            .collect(
                Collectors.toMap(BookLibraryCount::getLibraryId, BookLibraryCount::getBookCount));

        // Then
        assertThat(result).containsEntry(library1.getId(), 2);
    }
}