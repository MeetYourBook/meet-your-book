package com.meetyourbook.spec;

import com.meetyourbook.entity.Book;
import com.meetyourbook.entity.BookLibrary;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.List;
import java.util.UUID;
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
        return (root, query, cb) -> {
            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<Book> subRoot = subquery.from(Book.class);
            Join<Book, BookLibrary> subJoin = subRoot.join("bookLibraries");

            subquery.select(subRoot.get("id"))
                .where(subJoin.get("library").get("id").in(libraryIds));

            query.distinct(true);

            return root.get("id").in(subquery);
        };
    }

}
