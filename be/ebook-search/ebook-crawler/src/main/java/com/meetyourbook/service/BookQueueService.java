package com.meetyourbook.service;

import com.meetyourbook.dto.BookInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookQueueService {

    private static final int BATCH_SIZE = 10000;

    private final BlockingQueue<BookInfo> queue = new LinkedBlockingQueue<>();
    private final BookService bookService;

    public void addBookInfosToQueue(List<BookInfo> bookInfos) {
        bookInfos.forEach(this::addToQueue);
    }

    public void addToQueue(BookInfo bookInfo) {
        queue.offer(bookInfo);
    }

    @Scheduled(fixedDelay = 1000)
    @Async
    @LogExecutionTime
    public void processBatch() {
        List<BookInfo> batch = new ArrayList<>(BATCH_SIZE);
        queue.drainTo(batch, BATCH_SIZE);

        if (batch.isEmpty()) {
            return;
        }

        log.info("배치 작업 실행: {}권 저장", batch.size());
        bookService.saveAll(batch);
    }

}
