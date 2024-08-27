package com.meetyourbook.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "book_library", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"book_id", "library_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class BookLibrary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "library_id")
    private Library library;

    @JoinColumn(name = "url")
    private String url;

    public BookLibrary(Book book, Library library, String url) {
        this.book = book;
        this.library = library;
        this.url = url;
    }

    public static BookLibrary createBookLibrary(Book book, Library library, String url) {
        BookLibrary bookLibrary = new BookLibrary(book, library, url);
        book.addBookLibrary(bookLibrary);
        return bookLibrary;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookLibrary that = (BookLibrary) o;
        return Objects.equals(book, that.book) && Objects.equals(library,
            that.library);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, library);
    }
}
