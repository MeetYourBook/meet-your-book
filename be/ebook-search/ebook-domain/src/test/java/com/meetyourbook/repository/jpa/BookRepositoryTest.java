package com.meetyourbook.repository.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.meetyourbook.common.JpaRepositoryTest;
import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.BookLibrary;
import com.meetyourbook.entity.Library;
import com.meetyourbook.spec.BookSpecs;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

@JpaRepositoryTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    LibraryRepository libraryRepository;
    @Autowired
    BookLibraryRepository bookLibraryRepository;

    @DisplayName("제목, 저자, 출판사, 출판일로 책을 검색할 수 있다")
    @Test
    void findByBookInfo() {
        //given
        Book book1 = saveBook("지식의 착각", "스티븐 슬로먼", "교보문고", LocalDate.of(2022, 7, 11));

        BookInfo bookInfo = BookInfo.builder()
            .title("지식의 착각")
            .author("스티븐 슬로먼")
            .publisher("교보문고")
            .publishDate(LocalDate.of(2022, 7, 11))
            .build();

        //when
        Optional<Book> firstByBookInfo = bookRepository.findBooksByBookInfo(bookInfo);

        //then
        assertThat(firstByBookInfo)
            .isPresent()
            .get()
            .isEqualTo(book1);
    }

    @DisplayName("특정 도서관이 보유한 책을 검색할 수 있다.")
    @Test
    void findBooksInLibraries() {
        //given
        Book book1 = saveBook("지식의 착각", "스티븐 슬로먼", "교보문고", LocalDate.of(2022, 7, 11));
        Book book2 = saveBook("이기적 유전자", "리처드 도킨스", "교보문고", LocalDate.of(2022, 7, 11));
        Book book3 = saveBook("Effective Java", "죠슈아", "교보문고", LocalDate.of(2022, 7, 11));

        Library library1 = saveLibrary("대구가톨릭대학교");
        Library library2 = saveLibrary("서울대학교");
        Library library3 = saveLibrary("경기도서관");

        linkBookToLibrary(book1, library1);
        linkBookToLibrary(book1, library2);
        linkBookToLibrary(book1, library3);
        linkBookToLibrary(book2, library1);
        linkBookToLibrary(book2, library2);
        linkBookToLibrary(book3, library2);

        Specification<Book> spec = BookSpecs.inLibraries(List.of(library1.getId()));

        //when
        List<Book> books = bookRepository.findAll(spec);

        //then
        assertThat(books).hasSize(2).extracting(Book::getTitle)
            .containsExactlyInAnyOrder("지식의 착각", "이기적 유전자");
    }

    @DisplayName("특정 도서관이 보유한 책을 제목, 저자로 검색할 수 있다.")
    @Test
    void findBooksByTitleInLibraries() {
        //given
        Book book1 = saveBook("지식의 착각", "스티븐 슬로먼", "교보문고", LocalDate.of(2022, 7, 11));
        Book book2 = saveBook("지식의 착각", "리처드 도킨스", "교보문고", LocalDate.of(2022, 7, 11));
        Book book3 = saveBook("Effective Java", "죠슈아", "교보문고", LocalDate.of(2022, 7, 11));

        Library library1 = saveLibrary("대구가톨릭대학교");
        Library library2 = saveLibrary("서울대학교");
        Library library3 = saveLibrary("경기도서관");

        linkBookToLibrary(book1, library1);
        linkBookToLibrary(book1, library2);
        linkBookToLibrary(book1, library3);
        linkBookToLibrary(book2, library1);
        linkBookToLibrary(book2, library2);
        linkBookToLibrary(book3, library2);

        Specification<Book> spec = BookSpecs.inLibraries(List.of(library2.getId()))
            .and(BookSpecs.titleContains("지식의 착각"))
            .and(BookSpecs.authorContains("스티븐 슬로먼"));

        //when
        List<Book> books = bookRepository.findAll(spec);

        //then
        assertThat(books).hasSize(1).extracting(Book::getTitle, Book::getAuthor)
            .containsExactlyInAnyOrder(tuple("지식의 착각", "스티븐 슬로먼"));
    }

    private void linkBookToLibrary(Book book, Library library) {
        BookLibrary bookLibrary = BookLibrary.createBookLibrary(book, library, "url");
        bookLibraryRepository.save(bookLibrary);
    }

    private Library saveLibrary(String name) {
        Library library = Library.builder()
            .name(name)
            .build();
        return libraryRepository.save(library);
    }

    private Book saveBook(String title, String author, String publisher,
        LocalDate publishDate) {
        Book book = Book.builder()
            .title(title)
            .author(author)
            .publisher(publisher)
            .publishDate(publishDate)
            .build();
        return bookRepository.save(book);
    }

}