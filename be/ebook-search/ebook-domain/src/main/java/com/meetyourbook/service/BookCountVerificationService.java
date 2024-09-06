package com.meetyourbook.service;

import com.meetyourbook.dto.BookCountVerificationResult;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.jpa.BookLibraryRepository;
import com.meetyourbook.repository.jpa.BookLibraryRepository.BookLibraryCount;
import com.meetyourbook.repository.jpa.LibraryRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCountVerificationService {

    private final BookLibraryRepository bookLibraryRepository;
    private final LibraryRepository libraryRepository;

    @Transactional(readOnly = true)
    public List<BookCountVerificationResult> checkBookCount() {
        Map<Long, Integer> bookLibraryCounts = bookLibraryRepository.getBookLibraryCount()
            .stream()
            .collect(
                Collectors.toMap(BookLibraryCount::getLibraryId, BookLibraryCount::getBookCount));
        List<Library> libraries = libraryRepository.findAll();

        return libraries.stream()
            .map(library -> BookCountVerificationResult.of(library, bookLibraryCounts))
            .toList();

    }

}
