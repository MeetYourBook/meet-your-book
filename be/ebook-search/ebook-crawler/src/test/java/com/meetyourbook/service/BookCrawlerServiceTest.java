package com.meetyourbook.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.meetyourbook.crawler.ProcessorFactory;
import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.LibraryType;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.micrometer.core.instrument.MeterRegistry;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import us.codecraft.webmagic.processor.PageProcessor;

class BookCrawlerServiceTest {

    @Mock
    private ProcessorFactory processorFactory;
    @Mock
    private LibraryService libraryService;
    @Mock
    private DataSource dataSource;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private PageProcessor pageProcessor;
    @Mock
    private HikariDataSource hikariDataSource;
    @Mock
    private HikariPoolMXBean hikariPoolMXBean;

    private BookCrawlerService bookCrawlerService;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(dataSource.unwrap(HikariDataSource.class)).thenReturn(hikariDataSource);
        when(hikariDataSource.getHikariPoolMXBean()).thenReturn(hikariPoolMXBean);
        when(processorFactory.getProcessor(anyString())).thenReturn(pageProcessor);

        bookCrawlerService = new BookCrawlerService(processorFactory, libraryService, dataSource,
            meterRegistry);
    }

    @Test
    @DisplayName("크롤러의 생명 주기 테스트")
    void testCrawlerLifeCycle() throws InterruptedException {

        // 도서관 데이터 준비
        Library ASMLLibrary = Library.builder()
            .name("ASML")
            .type(LibraryType.UNIVERSITY_LIBRARY)
            .libraryUrl("https://asml.dkyobobook.co.kr/main.ink")
            .build();

        Library NCLibrary = Library.builder()
            .name("NC")
            .type(LibraryType.UNIVERSITY_LIBRARY)
            .libraryUrl("https://ncsoft.dkyobobook.co.kr/main.ink")
            .build();

        when(libraryService.findAll()).thenReturn(List.of(ASMLLibrary, NCLibrary));

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger count = new AtomicInteger(0);

        doAnswer(invocation -> {
            count.incrementAndGet();
            latch.countDown();
            return null;
        }).when(pageProcessor).process(any());

        //크롤링 시작
        String crawlerId = bookCrawlerService.startCrawl("BookPageProcessor", 50, 20);
        assertNotEquals("Crawler is already running", crawlerId);

        assertEquals(2, latch.getCount());

    }
}