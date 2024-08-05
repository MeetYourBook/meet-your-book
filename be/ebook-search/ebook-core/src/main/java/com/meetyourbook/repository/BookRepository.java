package com.meetyourbook.repository;


import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.author = :author AND b.publisher = :publisher AND b.publishDate = :pubDate")
    Optional<Book> findFirstByBookInfo(String title, String author, String publisher, LocalDate pubDate);

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.bookLibraries bl JOIN FETCH bl.library l " +
        "WHERE lower(b.publisher) LIKE lower(concat('%', :query, '%')) " +
        "AND (:libraryIds IS EMPTY OR l.id IN :libraryIds)")
    List<Book> searchByPublisher(@Param("query") String query, @Param("libraryIds") List<Long> libraryIds);

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.bookLibraries bl JOIN FETCH bl.library l " +
        "WHERE (lower(b.title) LIKE lower(concat('%', :query, '%')) OR " +
        "       lower(b.author) LIKE lower(concat('%', :query, '%')) OR " +
        "       lower(b.publisher) LIKE lower(concat('%', :query, '%'))) " +
        "AND (:libraryIds IS EMPTY OR l.id IN :libraryIds)")
    List<Book> searchByAll(@Param("query") String query, @Param("libraryIds") List<Long> libraryIds);

}
