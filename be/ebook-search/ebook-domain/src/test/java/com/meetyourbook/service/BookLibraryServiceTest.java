package com.meetyourbook.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.BookLibrary;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.BookLibraryRepository;
import com.meetyourbook.repository.BookRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookLibraryServiceTest {

    @Mock
    private LibraryDomainService libraryDomainService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookLibraryRepository bookLibraryRepository;

    @InjectMocks
    private BookLibraryService bookLibraryService;

    private BookInfo bookInfo1;
    private BookInfo bookInfo2;
    private Book book1;
    private Book book2;
    private Library library;

    @BeforeEach
    void setUp() {
        bookInfo1 = BookInfo.builder()
            .title("Title1")
            .author("Author1")
            .publisher("Publisher1")
            .publishDate(LocalDate.of(2021, 1, 1))
            .bookUrl("http://library.com/book1")
            .baseUrl("http://library.com")
            .build();
        bookInfo2 = BookInfo.builder()
            .title("Title2")
            .author("Author2")
            .publisher("Publisher2")
            .publishDate(LocalDate.of(2022, 2, 2))
            .bookUrl("http://library.com/book2")
            .baseUrl("http://library.com")
            .build();

        book1 = Book.builder()
            .title("Title1")
            .author("Author1")
            .publisher("Publisher1")
            .publishDate(LocalDate.of(2021, 1, 1))
            .build();

        book2 = Book.builder()
            .title("Title2")
            .author("Author2")
            .publisher("Publisher2")
            .publishDate(LocalDate.of(2022, 2, 2))
            .build();

        library = Library.builder()
            .name("Library")
            .libraryUrl("http://library.com")
            .build();
    }

    @Test
    @DisplayName("책 정보 2개가 새로운 데이터일 때 모두 저장한다.")
    void saveAll_WithNewBooks_ShouldSaveNewBooksAndBookLibraries() {
        // Given
        when(bookRepository.findFirstByBookInfo(anyString(), anyString(), anyString(), any(
            LocalDate.class)))
            .thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book1).thenReturn(book2);
        when(libraryDomainService.findByBaseUrl(anyString())).thenReturn(library);
        when(bookLibraryRepository.findBookLibraryByBookIdAndLibraryId(any(), any()))
            .thenReturn(Optional.empty());

        // When
        bookLibraryService.saveAll(List.of(bookInfo1, bookInfo2));

        // Then
        verify(bookRepository, times(2)).save(any(Book.class));
        verify(bookLibraryRepository).saveAll(argThat(bookLibraries -> {
            List<BookLibrary> bookLibraryList = StreamSupport.stream(bookLibraries.spliterator(),
                    false)
                .toList();

            return bookLibraryList.size() == 2 &&
                bookLibraryList.stream()
                    .anyMatch(bl -> bl.getBook().getTitle().equals(book1.getTitle())) &&
                bookLibraryList.stream()
                    .anyMatch(bl -> bl.getBook().getTitle().equals(book2.getTitle())) &&
                bookLibraryList.stream().allMatch(bl -> bl.getLibrary().equals(library));
        }));
    }

    @Test
    @DisplayName("책 정보 2개가 이미 존재하는 데이터일 때 책 상세 url만 업데이트한다.")
    void saveAll_WithExistingBooks_ShouldUpdateBookUrl() {
        // Given
        when(bookRepository.findFirstByBookInfo(anyString(), anyString(), anyString(), any(
            LocalDate.class)))
            .thenReturn(Optional.of(book1)).thenReturn(Optional.of(book2));
        when(libraryDomainService.findByBaseUrl(anyString())).thenReturn(library);
        BookLibrary bookLibrary1 = new BookLibrary(book1, library, "http://library.com/oldbook1");
        BookLibrary bookLibrary2 = new BookLibrary(book2, library, "http://library.com/oldbook2");
        when(bookLibraryRepository.findBookLibraryByBookIdAndLibraryId(any(), any()))
            .thenReturn(Optional.of(bookLibrary1)).thenReturn(Optional.of(bookLibrary2));
        // When
        bookLibraryService.saveAll(List.of(bookInfo1, bookInfo2));

        // Then
        verify(bookRepository, never()).save(any(Book.class));
        assertThat(bookLibrary1.getUrl()).isEqualTo(bookInfo1.bookUrl());
        assertThat(bookLibrary2.getUrl()).isEqualTo(bookInfo2.bookUrl());

    }

    @Test
    @DisplayName("책 정보 2개 중 1개만 새로운 데이터일 때 1개만 저장한다. 나머지는 url만 업데이트한다.")
    void saveAll_WithOneNewBook_ShouldSaveOneBookAndUpdateAnotherBook() {
        // Given
        when(bookRepository.findFirstByBookInfo(anyString(), anyString(), anyString(), any(
            LocalDate.class)))
            .thenReturn(Optional.of(book1)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book2);
        when(libraryDomainService.findByBaseUrl(anyString())).thenReturn(library);
        BookLibrary bookLibrary1 = new BookLibrary(book1, library, "http://library.com/oldbook1");
        when(bookLibraryRepository.findBookLibraryByBookIdAndLibraryId(any(), any()))
            .thenReturn(Optional.of(bookLibrary1)).thenReturn(Optional.empty());

        // When
        bookLibraryService.saveAll(List.of(bookInfo1, bookInfo2));

        // Then
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookLibraryRepository).saveAll(argThat(bookLibraries -> {
            List<BookLibrary> bookLibraryList = StreamSupport.stream(bookLibraries.spliterator(),
                    false)
                .toList();

            return bookLibraryList.size() == 1 && bookLibraryList.stream()
                .anyMatch(bl -> bl.getBook().getTitle().equals(book2.getTitle()));
        }));
        assertThat(bookLibrary1.getUrl()).isEqualTo(bookInfo1.bookUrl());
    }
}