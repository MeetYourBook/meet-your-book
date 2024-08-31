package com.meetyourbook.service;

import com.meetyourbook.entity.Library;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryCacheService {

    private final LibraryDomainService libraryDomainService;

    @Transactional(readOnly = true)
    @Cacheable(value = "libraryCache", key = "#baseUrl")
    public Long getLibraryFromCache(String baseUrl) {
        Library library = libraryDomainService.findByBaseUrl(baseUrl);
        return library.getId();
    }

}
