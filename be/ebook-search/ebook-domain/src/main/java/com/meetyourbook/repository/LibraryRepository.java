package com.meetyourbook.repository;

import com.meetyourbook.entity.Library;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {

    Optional<Library> findByLibraryUrl_UrlContaining(String baseUrl);

    List<Library> findByIdBetween(Long startId, Long endId);
}
