package com.meetyourbook.service;

import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.LibraryRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public Library findByDomain(String domain) {
        return libraryRepository.findByUrlContaining(domain).orElseThrow(NoSuchElementException::new);
    }

}
