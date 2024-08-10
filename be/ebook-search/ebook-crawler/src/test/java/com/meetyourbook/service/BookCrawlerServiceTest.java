package com.meetyourbook.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.meetyourbook.common.exception.CrawlerAlreadyRunningException;
import com.meetyourbook.common.exception.CrawlerNotRunningException;
import com.meetyourbook.crawler.ProcessorFactory;
import com.meetyourbook.crawler.ProcessorType;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookCrawlerServiceTest {

    @Mock
    private ProcessorFactory processorFactory;
    @Mock
    private LibraryService libraryService;
    @Mock
    private DataSource dataSource;
    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private BookCrawlerService bookCrawlerService;
    private ExecutorService executorService;

    @BeforeEach
    void setUp() {
        bookCrawlerService = new BookCrawlerService(processorFactory, libraryService, dataSource,
            meterRegistry);
        BookCrawlerService.pageQueue.clear();
    }

    @Test
    @DisplayName("크롤링을 시작하면 실행된 크롤러의 ID를 반환한다.")
    void startCrawl_returnId() {
        // Given
        when(libraryService.findAll()).thenReturn(Collections.emptyList());

        // When
        String id = bookCrawlerService.startCrawl("BookPage", 1, 10);

        // Then
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("이미 크롤링이 진행 중일 때 크롤링을 시작하려고 하면 예외를 던진다.")
    void starCrawl_whenAlreadyRunning_throwException() throws InterruptedException {

        // Given
        int threadCount = 2;
        executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger exceptionCount = new AtomicInteger(0);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    latch.countDown();
                    latch.await(); // 모든 스레드가 준비될 때까지 대기
                    bookCrawlerService.startCrawl("BookPage", 1, 10);
                    successCount.incrementAndGet();
                } catch (CrawlerAlreadyRunningException e) {
                    exceptionCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS);

        // Then
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(exceptionCount.get()).isEqualTo(1);

        // Verify
        verify(processorFactory, times(1)).getProcessor(ProcessorType.BOOK);
    }

    @Test
    @DisplayName("크롤링이 실행중이지 않을 때 크롤링을 중지하면 예외를 던진다.")
    void stopCrawl_whenNotRunning_throwException() {
        // When
        Throwable throwable = catchThrowable(() -> bookCrawlerService.stopCrawl());

        // Then
        assertThat(throwable).isInstanceOf(CrawlerNotRunningException.class);
    }


}
