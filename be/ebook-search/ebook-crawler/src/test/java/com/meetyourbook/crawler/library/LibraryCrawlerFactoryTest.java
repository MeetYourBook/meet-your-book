package com.meetyourbook.crawler.library;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.common.annotation.EbookPlatformCrawler;
import com.meetyourbook.dto.LibraryCrawlerTarget;
import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.entity.Library.EbookPlatform;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class LibraryCrawlerFactoryTest {

    private LibraryCrawlerFactory libraryCrawlerFactory;

    private ObjectMapper objectMapper;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        libraryCrawlerFactory = new LibraryCrawlerFactory(
            List.of(new KyoboLibraryCrawler(restTemplate, objectMapper),
                new NoAnnotationCrawler()));
    }

    @Test
    @DisplayName("findByPlatformName 메서드는 EbookPlatform에 해당하는 크롤러를 반환한다.")
    void findByPlatformName_shouldReturnCorrectCrawler() {
        // When & Then
        assertThat(libraryCrawlerFactory.findByPlatformName(
            EbookPlatform.KYOBO) instanceof KyoboLibraryCrawler).isTrue();

    }

    @Test
    @DisplayName("findByPlatformName 메서드는 EbookPlatform에 해당하는 크롤러가 없으면 null을 반환한다.")
    void findByPlatformName_shouldReturnNull() {
        // When & Then
        assertThat(libraryCrawlerFactory.findByPlatformName(EbookPlatform.ALADIN)).isNull();
    }

    @Test
    @DisplayName("생성자는 EbookPlatformCrawler 애노테이션이 적용된 크롤러만 등록한다.")
    void constructor_shouldNotIncludeCrawlerWithoutAnnotation() {
        assertThat(libraryCrawlerFactory.findByPlatformName(EbookPlatform.YES24)).isNull();
    }

    @EbookPlatformCrawler(EbookPlatform.KYOBO)
    private static class KyoboLibraryCrawler implements EbookPlatformLibraryCrawler {

        private final RestTemplate restTemplate;
        private final ObjectMapper objectMapper;

        public KyoboLibraryCrawler(RestTemplate restTemplate, ObjectMapper objectMapper) {
            this.restTemplate = restTemplate;
            this.objectMapper = objectMapper;
        }

        @Override
        public List<LibraryCreationInfo> crawlLibrary(LibraryCrawlerTarget target) {
            return List.of();
        }
    }

    private static class NoAnnotationCrawler implements EbookPlatformLibraryCrawler {

        @Override
        public List<LibraryCreationInfo> crawlLibrary(LibraryCrawlerTarget target) {
            return List.of();
        }
    }
}