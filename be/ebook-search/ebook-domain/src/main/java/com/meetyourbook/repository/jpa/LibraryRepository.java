package com.meetyourbook.repository.jpa;

import com.meetyourbook.entity.Library;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findByLibraryUrl_UrlContaining(String baseUrl);

    List<Library> findByIdBetween(Long startId, Long endId);

    Page<Library> findByNameStartingWith(String name, Pageable pageable);
}
