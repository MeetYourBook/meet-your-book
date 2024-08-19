package com.meetyourbook.service;

import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateInfo;
import com.meetyourbook.entity.Library;
import com.meetyourbook.exception.ResourceNotFoundException;
import com.meetyourbook.repository.LibraryRepository;
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
    public void createLibraries(List<LibraryCreationInfo> libraryCreationInfos) {
        List<Library> libraries = libraryCreationInfos.stream()
            .map(LibraryCreationInfo::toEntity)
            .toList();
        libraryRepository.saveAll(libraries);
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

    private Library findLibrary(Long id) {
        return libraryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Library not found"));
    }
}
