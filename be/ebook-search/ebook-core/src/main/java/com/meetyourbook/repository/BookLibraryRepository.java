package com.meetyourbook.repository;

import com.meetyourbook.entity.BookLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLibraryRepository extends JpaRepository<BookLibrary, Long> {

}
