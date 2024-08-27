package com.meetyourbook.repository;


import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.entity.Book;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("SELECT b FROM Book b WHERE b.title = :#{#bookInfo.title} AND b.author = :#{#bookInfo.author} AND b.publisher = :#{#bookInfo.publisher} AND b.publishDate = :#{#bookInfo.publishDate}")
    Optional<Book> findBooksByBookInfo(@Param("bookInfo") BookInfo bookInfo);

    @EntityGraph(attributePaths = {"bookLibraries", "bookLibraries.library"})
    List<Book> findAll(Specification<Book> spec);

    @EntityGraph(attributePaths = {"bookLibraries", "bookLibraries.library"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}
