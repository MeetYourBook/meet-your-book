package com.meetyourbook.service;

import com.meetyourbook.AOP.LogExecutionTime;
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

    private static final int BATCH_SIZE = 200;

    private final BlockingQueue<BookInfo> queue = new LinkedBlockingQueue<>();
    private final BookService bookService;

    public void addBookInfosToQueue(List<BookInfo> bookInfos) {
        bookInfos.forEach(this::addToQueue);
    }

    public void addToQueue(BookInfo bookInfo) {
        queue.offer(bookInfo);
    }

    @Scheduled(fixedDelay = 1000)
    @Async("taskExecutor")
    @LogExecutionTime
    public void processBatch() {
        List<BookInfo> batch = new ArrayList<>(BATCH_SIZE);
        queue.drainTo(batch, BATCH_SIZE);
        log.info("배치 작업 시작: {}권", batch.size());

        if (batch.isEmpty()) {
            return;
        }

        bookService.saveAll(batch);
        log.info("배치 작업 실행 완료: {}권", batch.size());
    }

}
