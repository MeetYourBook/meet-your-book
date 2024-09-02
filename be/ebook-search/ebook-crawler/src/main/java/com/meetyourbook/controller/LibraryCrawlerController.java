package com.meetyourbook.controller;

import com.meetyourbook.dto.EbookPlatformCrawlRequest;
import com.meetyourbook.dto.ImportResult;
import com.meetyourbook.dto.LibraryCrawlerRequest;
import com.meetyourbook.service.LibraryCrawlerService;
import com.meetyourbook.service.LibraryImportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/library-crawler")
@RequiredArgsConstructor
public class LibraryCrawlerController {

    private final LibraryImportService libraryImportService;
    private final LibraryCrawlerService libraryCrawlerService;

    @PostMapping("/import")
    public ResponseEntity<?> saveLibraryFromJson(@RequestPart("file") MultipartFile file) {
        int importedCount = libraryImportService.importLibrariesFromJson(file);
        return ResponseEntity.ok(new ImportResult(importedCount, "도서관 정보를 성공적으로 저장했습니다."));
    }

    @PostMapping("/crawl")
    public ResponseEntity<?> startCrawling(@RequestBody LibraryCrawlerRequest request) {
        libraryCrawlerService.crawl(request);
        return ResponseEntity.ok("크롤링을 시작했습니다.");
    }

    @PostMapping("/crawl-api")
    public ResponseEntity<?> startCrawlingApi(
        @Valid @RequestBody EbookPlatformCrawlRequest request) {
        int crawledCount = libraryCrawlerService.crawLibrary(request);
        return ResponseEntity.ok("총 " + crawledCount + "개의 도서관 정보를 크롤링했습니다.");
    }
}
