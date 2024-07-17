package com.meetyourbook.repository;


import com.meetyourbook.entity.Book;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByTitleAndAuthorAndPublisherAndPublishDate(String title, String author, String publisher, LocalDate pubDate);

}
