package com.meetyourbook.repository;

import com.ebooksearch.common.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
