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

    public Library findByBaseUrl(String baseUrl) {
        return libraryRepository.findByLibraryUrl_UrlContaining(baseUrl).orElseThrow(
            () -> new NoSuchElementException("base Url not found = " + baseUrl));
    }

    public void saveLibraryFromJson(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LibraryCreation> libraryCreations = objectMapper.readValue(new File(filePath),
                new TypeReference<>() {
                });

            List<Library> libraries = libraryCreations.stream()
                .map(LibraryCreation::toEntity)
                .toList();

            libraryRepository.saveAll(libraries);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Library> findAll() {
        return libraryRepository.findAll();
    }
}
