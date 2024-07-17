package com.meetyourbook.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class Book {

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

    @OneToMany(mappedBy = "book", cascade = {CascadeType.PERSIST, CascadeType.REMOVE} , orphanRemoval = true)
    private List<BookLibrary> bookLibraries = new ArrayList<>();

    @Builder
    private Book(String title, String author, String provider, String publisher, LocalDate publishDate,
        String imageUrl) {
        this.title = title;
        this.author = author;
        this.provider = provider;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.imageUrl = imageUrl;
    }

    public void add(Library library) {
        BookLibrary bookLibrary = new BookLibrary(this, library);
        if (!bookLibraries.contains(bookLibrary)) {
            bookLibraries.add(bookLibrary);
        }
    }

    public void remove(BookLibrary bookLibrary) {
        bookLibraries.remove(bookLibrary);
    }
}

