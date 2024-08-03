package com.meetyourbook.controller;

import com.meetyourbook.dto.CrawlerRequest;
import com.meetyourbook.service.BookCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawler")
@RequiredArgsConstructor
public class BookCrawlerController {

    private final BookCrawlerService bookCrawlerService;

    @PostMapping("/start")
    public ResponseEntity<String> startCrawler(@RequestBody CrawlerRequest request) {
        String crawlerId = bookCrawlerService.startCrawl(request.processor(), request.maxUrl(), request.viewCount());
        return ResponseEntity.ok("크롤러가 시작되었습니다. ID: " + crawlerId);
    }

    @GetMapping("/stop")
    public ResponseEntity<String> stopCrawler() {
        bookCrawlerService.stopCrawl();
        return ResponseEntity.ok("크롤러가 멈췄습니다.");
    }
}
