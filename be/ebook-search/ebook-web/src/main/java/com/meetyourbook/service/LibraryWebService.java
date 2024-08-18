package com.meetyourbook.service;

import com.meetyourbook.dto.LibraryCreateRequest;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateRequest;
import com.meetyourbook.entity.Library;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryWebService {

    private final LibraryDomainService libraryDomainService;

    public List<Library> findAll() {
        return libraryDomainService.findAll();
    }

    public Long createLibrary(LibraryCreateRequest request) {
        return libraryDomainService.createLibrary(request.toLibraryCreationInfo());
    }

    public LibraryResponse findById(long id) {
        return libraryDomainService.findById(id);
    }

    public LibraryResponse updateLibrary(Long id, LibraryUpdateRequest request) {
        return libraryDomainService.updateLibrary(id, request.toLibraryUpdateInfo());
    }

    public void delete(Long id) {
        libraryDomainService.delete(id);
    }

    public List<LibraryResponse> findAllLibraryResponses() {
        List<Library> libraries = findAll();

        return libraries.stream()
            .map(LibraryResponse::fromEntity)
            .toList();
    }

}
