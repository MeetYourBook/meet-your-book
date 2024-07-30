package com.meetyourbook.service;

import com.meetyourbook.aop.LogExecutionTime;
import com.meetyourbook.dto.BookInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookQueueService {

    private static final int BATCH_SIZE = 20000;
    private static final int PROCESSING_INTERVAL = 10000; // 10 seconds

    private final BlockingQueue<BookInfo> queue = new LinkedBlockingQueue<>();
    private final BookLibraryService bookService;

    public void addBookInfosToQueue(List<BookInfo> bookInfos) {
        queue.addAll(bookInfos);
        log.info("Added {} books to queue. Current queue size: {}", bookInfos.size(), queue.size());
    }

    @Scheduled(fixedDelay = PROCESSING_INTERVAL)
    @Async("taskExecutor")
    @LogExecutionTime
    public void processBatch() {
        List<BookInfo> batch = fetchBatchFromQueue();
        if (batch.isEmpty()) {
            log.info("배치 작업 생략");
            return;
        }
        log.info("배치 작업 시작: {}권", batch.size());
        saveBatchWithErrorHandling(batch);
    }

    private List<BookInfo> fetchBatchFromQueue() {
        List<BookInfo> batch = new ArrayList<>(BATCH_SIZE);
        queue.drainTo(batch, BATCH_SIZE);
        log.info("Queue size: {}", queue.size());
        return batch;
    }

    private void saveBatchWithErrorHandling(List<BookInfo> batch) {
        try {
            bookService.saveAll(batch);
            log.info("배치 작업 실행 완료: {}권", batch.size());
        } catch (Exception e) {
            log.error("배치 작업 실행 중 오류 발생: ", e);
        }
    }

}
