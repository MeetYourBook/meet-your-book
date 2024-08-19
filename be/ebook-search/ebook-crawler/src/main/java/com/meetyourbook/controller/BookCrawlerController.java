package com.meetyourbook.controller;

import com.meetyourbook.dto.CrawlerRequest;
import com.meetyourbook.dto.CrawlerResponse;
import com.meetyourbook.service.BookCrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawler")
@RequiredArgsConstructor
@Slf4j
public class BookCrawlerController {

    private final BookCrawlerService bookCrawlerService;

    @PostMapping("/start")
    public ResponseEntity<CrawlerResponse> startCrawler(@RequestBody CrawlerRequest request) {
        String id = bookCrawlerService.startCrawl(request.processor(), request.initMaxUrl(),
            request.viewCount());
        return ResponseEntity.ok(new CrawlerResponse(id, "크롤러가 시작되었습니다."));
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<CrawlerResponse> stopCrawler(@PathVariable String id) {
        bookCrawlerService.stopCrawl();
        return ResponseEntity.ok(new CrawlerResponse(id, "크롤러가 중단되었습니다."));
    }
}