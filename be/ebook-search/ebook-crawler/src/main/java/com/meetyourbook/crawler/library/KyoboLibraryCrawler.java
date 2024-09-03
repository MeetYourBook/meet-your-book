package com.meetyourbook.crawler.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.common.annotation.EbookPlatformCrawler;
import com.meetyourbook.dto.LibraryCrawlResponse;
import com.meetyourbook.dto.LibraryCrawlResult;
import com.meetyourbook.dto.LibraryCrawlerTarget;
import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.entity.Library.EbookPlatform;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@EbookPlatformCrawler(EbookPlatform.KYOBO)
@RequiredArgsConstructor
@Slf4j
public class KyoboLibraryCrawler implements EbookPlatformLibraryCrawler {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public List<LibraryCreationInfo> crawlLibrary(LibraryCrawlerTarget target) {
        URI uri = URI.create(target.url());
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                LibraryCrawlResponse libraryCrawlResponse = objectMapper.readValue(
                    response.getBody(),
                    LibraryCrawlResponse.class);
                List<LibraryCrawlResult> results = libraryCrawlResponse.resultData().item();

                return results.stream()
                    .filter(result -> !result.libraryHost().contains("dev"))
                    .filter(result -> !result.libraryHost().contains("test"))
                    .filter(result -> !result.libraryName().contains("test"))
                    .filter(result -> !result.libraryName().contains("테스트"))
                    .map(result -> result.toLibraryCreationInfo(target.platform()))
                    .toList();
            } else {
                throw new IllegalStateException("다음 API에 대해 응답 실패: " + response.getStatusCode());
            }
        } catch (JsonProcessingException e) {
            log.error("API 호출 중 오류 발생: {}", e);
        }
        return List.of();
    }
}
