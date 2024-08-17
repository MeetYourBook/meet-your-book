package com.meetyourbook.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateInfo;
import com.meetyourbook.entity.Library;
import com.meetyourbook.exception.ResourceNotFoundException;
import com.meetyourbook.repository.LibraryRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryDomainService {

    private final LibraryRepository libraryRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<Library> findAll() {
        return libraryRepository.findAll();
    }

    @Transactional
    public Long createLibrary(LibraryCreationInfo creationInfo) {
        Library library = libraryRepository.save(creationInfo.toEntity());
        return library.getId();
    }

    @Transactional
    public LibraryResponse findById(long id) {
        Library library = findLibrary(id);
        return LibraryResponse.fromEntity(library);
    }

    @Transactional
    public LibraryResponse updateLibrary(Long id, LibraryUpdateInfo updateInfo) {
        Library library = findLibrary(id);
        library.update(updateInfo);
        return LibraryResponse.fromEntity(library);
    }

    @Transactional
    public void delete(Long id) {
        Library library = findLibrary(id);
        libraryRepository.delete(library);
    }

    @Transactional(readOnly = true)
    public List<LibraryResponse> findAllLibraryResponses() {
        List<Library> libraries = findAll();

        return libraries.stream()
            .map(LibraryResponse::fromEntity)
            .toList();
    }

    @Transactional
    public void updateTotalBookCount(int totalBookCount, String baseUrl) {
        Library library = findByBaseUrl(baseUrl);
        library.updateTotalBookCount(totalBookCount);
    }

    @Transactional(readOnly = true)
    public Library findByBaseUrl(String baseUrl) {
        return libraryRepository.findByLibraryUrl_UrlContaining(baseUrl)
            .orElseThrow(() -> new NoSuchElementException("base Url not found = " + baseUrl));
    }

    @Transactional
    public void saveLibraryFromJson(String filePath) {
        try {
            List<LibraryCreationInfo> libraryCreationInfos = readLibraryCreationsFromJson(filePath);
            List<Library> libraries = convertToLibraries(libraryCreationInfos);
            libraryRepository.saveAll(libraries);
            log.info("Successfully saved {} libraries", libraries.size());
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read library data from JSON", e);
        }
    }

    private List<LibraryCreationInfo> readLibraryCreationsFromJson(String filePath)
        throws IOException {
        return objectMapper.readValue(new File(filePath), new TypeReference<>() {
        });
    }

    private List<Library> convertToLibraries(List<LibraryCreationInfo> libraryCreationInfos) {
        return libraryCreationInfos.stream()
            .map(LibraryCreationInfo::toEntity)
            .toList();
    }

    private Library findLibrary(Long id) {
        return libraryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
    }
}
