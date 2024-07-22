package com.meetyourbook.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.dto.LibraryCreation;
import com.meetyourbook.entity.Library;
import com.meetyourbook.repository.LibraryRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

    public void saveLibraryFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LibraryCreation> libraryCreations = objectMapper.readValue(new File(filePath),
                new TypeReference<List<LibraryCreation>>() {
                });

            List<Library> libraries = libraryCreations.stream()
                .map(LibraryCreation::toEntity)
                .toList();

            libraryRepository.saveAll(libraries);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
