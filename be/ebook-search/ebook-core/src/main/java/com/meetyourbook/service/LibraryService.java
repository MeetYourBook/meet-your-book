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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final ObjectMapper objectMapper;

    public Library findByBaseUrl(String baseUrl) {
        return libraryRepository.findByLibraryUrl_UrlContaining(baseUrl)
            .orElseThrow(() -> new NoSuchElementException("base Url not found = " + baseUrl));
    }

    public void saveLibraryFromJson(String filePath) {
        try {
            List<LibraryCreation> libraryCreations = readLibraryCreationsFromJson(filePath);
            List<Library> libraries = convertToLibraries(libraryCreations);
            libraryRepository.saveAll(libraries);
            log.info("Successfully saved {} libraries", libraries.size());
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read library data from JSON", e);
        }
    }

    private List<LibraryCreation> readLibraryCreationsFromJson(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath), new TypeReference<>() {});
    }

    private List<Library> convertToLibraries(List<LibraryCreation> libraryCreations) {
        return libraryCreations.stream()
            .map(LibraryCreation::toEntity)
            .toList();
    }

    public List<Library> findAll() {
        return libraryRepository.findAll();
    }
}
