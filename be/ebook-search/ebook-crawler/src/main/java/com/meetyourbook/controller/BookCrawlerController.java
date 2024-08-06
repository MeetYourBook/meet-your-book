package com.meetyourbook.controller;

import com.meetyourbook.dto.CrawlerErrorResponse;
import com.meetyourbook.dto.CrawlerRequest;
import com.meetyourbook.dto.CrawlerResponse;
import com.meetyourbook.service.BookCrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BookCrawler", description = "도서 크롤러 API")
@Slf4j
@RestController
@RequestMapping("/api/crawler")
@RequiredArgsConstructor
public class BookCrawlerController {

    private final BookCrawlerService bookCrawlerService;

    @Operation(
        summary = "크롤러 시작",
        description = "새로운 크롤링 작업을 시작합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "크롤러 시작 성공",
            content = @Content(schema = @Schema(implementation = CrawlerResponse.class),
                examples = @ExampleObject(value = "{\"id\":\"crawler-123\",\"message\":\"크롤러가 시작되었습니다.\"}"))
        ),
        @ApiResponse(responseCode = "500", description = "크롤러 시작 실패",
            content = @Content(schema = @Schema(implementation = CrawlerErrorResponse.class),
                examples = @ExampleObject(value = "{\"status\":\"500\",\"message\":\"현재 크롤러가 이미 실행중입니다.\",\"timestamp\":\"2024-08-06T12:00:00Z\"}"))
        )
    })
    @PostMapping("/start")
    public ResponseEntity<CrawlerResponse> startCrawler(@RequestBody CrawlerRequest request) {
        String id = bookCrawlerService.startCrawl(request.processor(), request.initMaxUrl(),
            request.viewCount());
        return ResponseEntity.ok(new CrawlerResponse(id, "크롤러가 시작되었습니다."));
    }

    @Operation(
        summary = "크롤러 중지",
        description = "현재 실행 중인 크롤링 작업을 중지합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "크롤러 중지 성공",
            content = @Content(schema = @Schema(implementation = CrawlerResponse.class),
                examples = @ExampleObject(value = "{\"id\":\"crawler-123\",\"message\":\"크롤러가 중단되었습니다.\"}"))
        ),
        @ApiResponse(responseCode = "500", description = "크롤러 중지 실패",
            content = @Content(schema = @Schema(implementation = CrawlerErrorResponse.class),
                examples = @ExampleObject(value = "{\"status\":\"500\",\"message\":\"현재 크롤러가 실행중이지 않습니다.\",\"timestamp\":\"2024-08-06T12:00:00Z\"}"))
        )
    })
    @GetMapping("/{id}/stop")
    public ResponseEntity<CrawlerResponse> stopCrawler(@PathVariable String id) {
        bookCrawlerService.stopCrawl(id);
        return ResponseEntity.ok(new CrawlerResponse(id, "크롤러가 중단되었습니다."));
    }
}
