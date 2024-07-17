package com.meetyourbook.repository;

import com.meetyourbook.entity.Library;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findByUrlContaining(String domain);

}
