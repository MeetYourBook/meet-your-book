package com.meetyourbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryImportService {

    private final LibraryDomainService libraryDomainService;

    public void saveLibraryFromJson(String path) {
        libraryDomainService.saveLibraryFromJson(path);
    }

}
