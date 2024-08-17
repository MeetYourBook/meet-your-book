package com.meetyourbook.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.dto.BookCreationInfo;
import com.meetyourbook.dto.BookUpdateInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.repository.BookRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookDomainServiceTest {

    @Autowired
    BookDomainService bookDomainService;
    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("책 정보를 입력받아 책 정보를 저장한다.")
    void createBook() {
        //given
        BookCreationInfo request = BookCreationInfo.builder()
            .title("제목")
            .author("저자")
            .build();

        //when
        UUID bookId = bookDomainService.createBook(request);
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        //then
        assertThat(optionalBook)
            .isPresent()
            .get()
            .extracting(Book::getTitle, Book::getAuthor)
            .containsExactly(request.title(), request.author());
    }

    @Test
    @DisplayName("책 정보를 업데이트할 수 있다.")
    void updateBook() {
        //given
        Book book = Book.builder()
            .title("해리포터")
            .author("J.K 롤링")
            .publisher("민음사")
            .publishDate(LocalDate.of(2004, 4, 4))
            .build();
        bookRepository.save(book);

        BookUpdateInfo request = BookUpdateInfo.builder()
            .title("해리포터")
            .author("J.K 롤링")
            .publisher("지식하우스")
            .publishDate(LocalDate.of(2003, 3, 3))
            .build();

        //when
        bookDomainService.updateBook(book.getId(), request);
        Optional<Book> optionalBook = bookRepository.findById(book.getId());

        //then
        assertThat(optionalBook)
            .isPresent()
            .get()
            .extracting(Book::getTitle, Book::getAuthor, Book::getPublisher, Book::getPublishDate)
            .containsExactly(
                request.title(),
                request.author(),
                request.publisher(),
                request.publishDate()
            );
    }


}