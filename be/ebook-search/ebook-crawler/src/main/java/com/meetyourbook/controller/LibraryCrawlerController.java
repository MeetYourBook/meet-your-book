package com.meetyourbook.controller;

import com.meetyourbook.dto.ImportResult;
import com.meetyourbook.service.LibraryImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/library-crawler")
@RequiredArgsConstructor
public class LibraryCrawlerController {

    private final LibraryImportService libraryImportService;

    @PostMapping("/import")
    public ResponseEntity<?> saveLibraryFromJson(@RequestParam("file") MultipartFile file) {
        int importedCount = libraryImportService.importLibrariesFromJson(file);
        return ResponseEntity.ok(new ImportResult(importedCount, "도서관 정보를 성공적으로 저장했습니다."));
    }
}
