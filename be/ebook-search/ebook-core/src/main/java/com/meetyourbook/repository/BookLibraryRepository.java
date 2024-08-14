package com.meetyourbook.repository;

import com.meetyourbook.entity.BookLibrary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookLibraryRepository extends JpaRepository<BookLibrary, Long> {

    @Query("SELECT bl.library.id as libraryId, COUNT(bl.id) as bookCount FROM BookLibrary bl GROUP BY bl.library.id")
    List<BookLibraryCount> getBookLibraryCount();

    interface BookLibraryCount {

        Long getLibraryId();

        Integer getBookCount();
    }

}
