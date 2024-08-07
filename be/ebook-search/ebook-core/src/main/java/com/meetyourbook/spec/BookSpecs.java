package com.meetyourbook.spec;

import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.BookLibrary;
import jakarta.persistence.criteria.Join;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> authorContains(String author) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> publisherContains(String publisher) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(criteriaBuilder.lower(root.get("publisher")), "%" + publisher.toLowerCase() + "%");
    }

    public static Specification<Book> inLibraries(List<Long> libraryIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Book, BookLibrary> bookLibraryJoin = root.join("bookLibraries");
            return bookLibraryJoin.get("library").get("id").in(libraryIds);
        };
    }

}
