package com.meetyourbook.entity;


import com.meetyourbook.dto.BookUpdateInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator(style = Style.TIME)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private String title;
    private String author;
    private String provider;
    private String publisher;
    private LocalDate publishDate;
    private String imageUrl;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final Set<BookLibrary> bookLibraries = new HashSet<>();

    @Builder
    private Book(String title, String author, String provider, String publisher,
        LocalDate publishDate,
        String imageUrl) {
        this.title = title;
        this.author = author;
        this.provider = provider;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
    }

    public void addBookLibrary(BookLibrary bookLibrary) {
        bookLibraries.add(bookLibrary);
    }

    public void remove(BookLibrary bookLibrary) {
        bookLibraries.remove(bookLibrary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(author,
            book.author) && Objects.equals(publisher, book.publisher) && Objects.equals(
            publishDate, book.publishDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, provider, publisher, publishDate, imageUrl);
    }

    public void update(BookUpdateInfo request) {
        this.title = request.title();
        this.author = request.author();
        this.provider = request.provider();
        this.publisher = request.publisher();
        this.publishDate = request.publishDate();
        this.imageUrl = request.imageUrl();
    }
}
