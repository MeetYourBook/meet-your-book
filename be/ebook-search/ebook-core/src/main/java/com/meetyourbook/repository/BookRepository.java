package com.meetyourbook.repository;


import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author = :author AND b.publisher = :publisher AND b.publishDate = :pubDate")
    Optional<Book> findFirstByBookInfo(String title, String author, String publisher, LocalDate pubDate);

    @EntityGraph(attributePaths = {"bookLibraries", "bookLibraries.library"})
    List<Book> findAll(Specification<Book> spec);

    @EntityGraph(attributePaths = {"bookLibraries", "bookLibraries.library"})
    List<Book> findAll(Specification<Book> spec, Pageable pageable);
}
