package com.meetyourbook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.crawler.library.EbookPlatformLibraryCrawler;
import com.meetyourbook.crawler.library.LibraryCrawlerFactory;
import com.meetyourbook.dto.EbookPlatformCrawlRequest;
import com.meetyourbook.dto.LibraryCrawlResponse;
import com.meetyourbook.dto.LibraryCrawlResult;
import com.meetyourbook.dto.LibraryCrawlerRequest;
import com.meetyourbook.dto.LibraryCrawlerTarget;
import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.dto.LibraryCreationResult;
import com.meetyourbook.dto.SimpleLibraryInfo;
import com.meetyourbook.entity.Library.EbookPlatform;
import com.meetyourbook.util.EbookPlatformUrlProperties;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryCrawlerService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final LibraryDomainService libraryDomainService;
    private final EbookPlatformUrlProperties ebookPlatformUrlProperties;
    private final LibraryCrawlerFactory libraryCrawlerFactory;

    public void crawl(LibraryCrawlerRequest request) {
        log.info("도서관 정보 크롤링을 시작합니다.");
        List<String> characters = generateCharacters();
        Set<SimpleLibraryInfo> libraryNames = new HashSet<>();
        String libraryBaseUrl = request.libraryBaseUrl();

        for (String character : characters) {
            try {
                crawlCharacter(character, libraryNames, libraryBaseUrl);
                Thread.sleep(request.crawlInterval());
            } catch (RuntimeException e) {
                log.error("다음 문자에 대한 크롤링 중 오류 발생: {}", character, e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("크롤링이 중단되었습니다: {}", e.getMessage());
                break;
            }
        }

        log.info("총 {}개의 도서관 정보를 수집하였습니다.", libraryNames.size());
        saveToJson(libraryNames, request.libraryBaseUrl());
    }

    public LibraryCreationResult crawLibrary(EbookPlatformCrawlRequest request) {
        EbookPlatform ebookPlatform = EbookPlatform.valueOf(request.ebookPlatform().toUpperCase());
        String targetUrl = determineUrl(request.url(), ebookPlatform);
        EbookPlatformLibraryCrawler crawler = libraryCrawlerFactory.findByPlatformName(
            ebookPlatform);
        LibraryCrawlerTarget target = new LibraryCrawlerTarget(ebookPlatform, targetUrl);
        List<LibraryCreationInfo> libraryCreationInfos = crawler.crawlLibrary(target);
        return libraryDomainService.createLibraries(libraryCreationInfos);
    }


    private String determineUrl(String url, EbookPlatform ebookPlatform) {
        if (url != null) {
            return url;
        }
        return ebookPlatformUrlProperties.getPlatformUrls().get(ebookPlatform);
    }

    private void crawlCharacter(String character, Set<SimpleLibraryInfo> libraryNames,
        String baseUrl) {
        try {
            URI uri = buildLibraryUri(baseUrl, character);
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                processResponse(response.getBody(), character, libraryNames);
            } else {
                throw new IllegalStateException(
                    "다음 syllable에 대해 응답 실패 " + character + ", 응답코드: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("API 호출 중 오류 발생: {}", character, e);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 중 오류 발생: {}", character, e);
        }
    }

    private URI buildLibraryUri(String baseUrl, String syllable) {
        String encodedSyllable = URLEncoder.encode(syllable, StandardCharsets.UTF_8);
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
            .queryParam("device_type", "M")
            .queryParam("item_position", "1")
            .queryParam("keyword", encodedSyllable)
            .queryParam("searchtype", "4")
            .queryParam("type_cd", "3")
            .queryParam("view_count", "5000")
            .build(true)
            .toUri();
    }

    private void processResponse(String responseBody, String character,
        Set<SimpleLibraryInfo> libraryNames) throws JsonProcessingException {
        LibraryCrawlResponse libraryCrawlResponse = objectMapper.readValue(responseBody,
            LibraryCrawlResponse.class);

        if (libraryCrawlResponse.resultData().totalCount().equals("0")) {
            log.info("다음 {}에 대해서 도서관 정보가 없습니다.", character);
            return;
        }

        List<LibraryCrawlResult> libraryCrawlResults = libraryCrawlResponse.resultData()
            .item();
        libraryCrawlResults.stream()
            .map(libraryCrawlResult -> new SimpleLibraryInfo(libraryCrawlResult.libraryName(),
                libraryCrawlResult.libraryHost()))
            .forEach(libraryNames::add);

        log.info("다음 {}에 대해서 총 {}개의 도서관 정보를 수집하였습니다.", character, libraryCrawlResults.size());
    }

    private List<String> generateCharacters() {
        List<String> characters = new ArrayList<>();

        for (char syllable = '가'; syllable <= '힣'; syllable++) {
            characters.add(String.valueOf(syllable));
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            characters.add(String.valueOf(c));
        }

        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(String.valueOf(c));
        }

        for (char c = '0'; c <= '9'; c++) {
            characters.add(String.valueOf(c));
        }

        return characters;
    }

    private void saveToJson(Set<SimpleLibraryInfo> libraryNames, String libraryBaseUrl) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(libraryBaseUrl), libraryNames);
            log.info("도서관 정보를 성공적으로 {}에 저장하였습니다.", libraryBaseUrl);
        } catch (IOException e) {
            log.error("JSON 파일 저장 중 오류 발생", e);
            throw new RuntimeException("JSON 파일 저장 중 오류 발생", e);
        }
    }
}