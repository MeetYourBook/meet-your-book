package com.meetyourbook.crawler;

import com.meetyourbook.entity.Library;
import com.meetyourbook.service.LibraryService;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookCrawlerRunner implements CommandLineRunner {

    private final ProcessorFactory processorFactory;
    private final LibraryService libraryService;
    private final DataSource dataSource;
    private final MeterRegistry meterRegistry;
    private static final int MAX_URL_TO_SEARCH = 1954;
    public static final ReentrantLock QUEUE_LOCK = new ReentrantLock();
    public static final Condition QUEUE_NOT_EMPTY = QUEUE_LOCK.newCondition();
    public static final Queue<String> pageQueue = new ConcurrentLinkedQueue<>();

    @Value("${crawler.processor}")
    private String processor;

    @Override
    public void run(String... args) {
        PageProcessor selectedProcessor = processorFactory.getProcessor(processor);
        List<Library> libraries = libraryService.findAll();

        pageQueue.addAll(
            libraries.stream()
                .filter(Library::hasMainInk)
                .map(library -> library.getUrlWithQueryParameters(1))
                .limit(MAX_URL_TO_SEARCH)
                .toList()
        );

        AtomicInteger pageCount = new AtomicInteger(0);
        ConcurrentMap<String, Boolean> visited = new ConcurrentHashMap<>();

        startResourceMonitoring();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (true) {
                QUEUE_LOCK.lock();
                try {
                    while (pageQueue.isEmpty()) {
                        if (executor.isTerminated()) {
                            QUEUE_LOCK.unlock();
                            break;
                        }
                        QUEUE_NOT_EMPTY.await(100, TimeUnit.MILLISECONDS);
                    }
                    if (pageQueue.isEmpty() && executor.isTerminated()) {
                        break;
                    }
                    String url = pageQueue.poll();
                    if (url != null && !visited.containsKey(url)) {
                        executor.submit(new BookCrawler(pageCount, visited, url, selectedProcessor));
                    }
                } finally {
                    QUEUE_LOCK.unlock();
                }
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Main thread interrupted!", e);
        }

    }

    private class BookCrawler implements Runnable {

        private final AtomicInteger count;
        private final ConcurrentMap<String, Boolean> visited;
        private final String url;
        private final PageProcessor pageProcessor;

        public BookCrawler(AtomicInteger count, ConcurrentMap<String, Boolean> visited, String url, PageProcessor pageProcessor) {
            this.count = count;
            this.visited = visited;
            this.url = url;
            this.pageProcessor = pageProcessor;
        }

        @Override
        public void run() {
            if (!visited.containsKey(url)) {
                Spider spider = Spider.create(pageProcessor)
                    .addUrl(url);

                visited.put(url, true);
                spider.run();
                count.incrementAndGet();

                signalQueue();
            }
        }

        private void signalQueue() {
            QUEUE_LOCK.lock();
            try {
                QUEUE_NOT_EMPTY.signalAll();
            } finally {
                QUEUE_LOCK.unlock();
            }
        }
    }

    private void startResourceMonitoring() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        log.info("pageQueue size: {}", pageQueue.size());
        scheduler.scheduleAtFixedRate(this::logResourceUsage, 1, 1, TimeUnit.SECONDS);

    }

    private void logResourceUsage() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);

        long virtualThreadCount = 0;
        long platformThreadCount = 0;

        for (ThreadInfo threadInfo : threadInfos) {
            if (threadInfo != null) {
                if (threadInfo.getThreadName().startsWith("VirtualThread")) {
                    virtualThreadCount++;
                } else {
                    platformThreadCount++;
                }
            }
        }

        HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
        int activeConnections = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
        int totalConnections = hikariDataSource.getHikariPoolMXBean().getTotalConnections();

        log.info("Resource Usage - Virtual Threads: {}, Platform Threads: {}, DB Connections: {}/{} (active/total)",
            virtualThreadCount, platformThreadCount, activeConnections, totalConnections);

        // Micrometer를 사용한 메트릭 기록
        meterRegistry.gauge("crawler.threads.virtual", virtualThreadCount);
        meterRegistry.gauge("crawler.threads.platform", platformThreadCount);
        meterRegistry.gauge("crawler.db.connections.active", activeConnections);
        meterRegistry.gauge("crawler.db.connections.total", totalConnections);
    }

}
