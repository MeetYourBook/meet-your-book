package com.meetyourbook.controller;

import com.meetyourbook.service.LibraryImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library-crawler")
@RequiredArgsConstructor
public class LibraryCrawlerController {

    private final LibraryImportService libraryImportService;

    @PostMapping("/save")
    public ResponseEntity<?> saveLibraryFromJson() {
        libraryImportService.saveLibraryFromJson(
            "/Users/jeonbyeong-ung/IdeaProjects/meet-your-book/be/ebook-search/ebook-crawler/src/main/resources/library_list.json");
        return ResponseEntity.ok().build();
    }
}
