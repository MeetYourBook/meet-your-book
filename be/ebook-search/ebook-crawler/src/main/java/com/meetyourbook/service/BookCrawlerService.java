package com.meetyourbook.service;

import com.meetyourbook.common.exception.CrawlerAlreadyRunningException;
import com.meetyourbook.common.exception.CrawlerNotRunningException;
import com.meetyourbook.crawler.ProcessorFactory;
import com.meetyourbook.crawler.ProcessorType;
import com.meetyourbook.entity.Library;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCrawlerService {

    public static final ReentrantLock QUEUE_LOCK = new ReentrantLock();
    public static final Condition QUEUE_NOT_EMPTY = QUEUE_LOCK.newCondition();
    public static final BlockingQueue<String> pageQueue = new LinkedBlockingQueue<>();
    public static final ConcurrentMap<String, Boolean> visited = new ConcurrentHashMap<>();

    private final ProcessorFactory processorFactory;
    private final LibraryDomainService libraryDomainService;
    private final DataSource dataSource;
    private final MeterRegistry meterRegistry;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicLong activeSpiderCount = new AtomicLong(0);
    private final ConcurrentHashMap<String, Spider> activeSpiders = new ConcurrentHashMap<>();
    private final ExecutorService crawlTaskExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private final ExecutorService spiderExecutor = Executors.newVirtualThreadPerTaskExecutor();


    public String startCrawl(String processor, int viewCount, Long startId,
        Long endId) {
        if (isRunning.compareAndSet(false, true)) {
            String id = String.valueOf(UUID.randomUUID());
            crawl(id, processor, viewCount, startId, endId);
            return id;
        } else {
            throw new CrawlerAlreadyRunningException();
        }
    }

    public void stopCrawl() {
        if (isRunning.compareAndSet(true, false)) {
            log.info("Executor를 멈춥니다.");
            crawlTaskExecutor.shutdownNow();
            spiderExecutor.shutdownNow();
            isRunning.set(false);
            stopAllSpiders();
            log.info("크롤러가 멈췄습니다.");
        } else {
            throw new CrawlerNotRunningException();
        }
    }

    private void crawl(String crawlerId, String processor, int viewCount,
        Long startId, Long endId) {
        PageProcessor selectedProcessor = processorFactory.getProcessor(
            ProcessorType.fromString(processor));
        List<Library> libraries = libraryDomainService.findLibrariesByIdRange(startId, endId);
        log.info("총 {}개의 도서관을 크롤링합니다.", libraries.size());
        log.info("도서관 범위는 {}부터 {}까지입니다.", startId, endId);

        pageQueue.addAll(
            libraries.stream()
                .filter(Library::hasMainInk)
                .map(library -> library.getUrlWithQueryParameters(viewCount))
                .toList()
        );

        startResourceMonitoring();
        try {
            while (isRunning.get() && (activeSpiderCount.get() != 0 || !pageQueue.isEmpty())) {
                QUEUE_LOCK.lock();
                try {
                    while (pageQueue.isEmpty()) {
                        if (!QUEUE_NOT_EMPTY.await(30, TimeUnit.SECONDS)) {
                            break;
                        }
                    }
                    String url = pageQueue.poll();
                    if (url != null && !visited.containsKey(url)) {
                        crawlTaskExecutor.submit(new BookCrawler(url, selectedProcessor));
                        activeSpiderCount.incrementAndGet();
                    }
                } finally {
                    QUEUE_LOCK.unlock();
                }
            }

            crawlTaskExecutor.shutdown();
            crawlTaskExecutor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Crawler {} was interrupted", crawlerId);
        } finally {
            crawlTaskExecutor.shutdownNow();
            isRunning.set(false);
            log.info("Crawler {} has stopped", crawlerId);
        }
    }

    private void stopAllSpiders() {
        activeSpiders.forEach((url, spider) -> {
            activeSpiders.remove(url);
            spider.stop();
        });
    }

    private void startResourceMonitoring() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
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

        log.info(
            "Resource Usage - Virtual Threads: {}, Platform Threads: {}, DB Connections: {}/{} (active/total)",
            virtualThreadCount, platformThreadCount, activeConnections, totalConnections);
        log.info("pageQueue size: {}", pageQueue.size());

        meterRegistry.gauge("crawler.threads.virtual", virtualThreadCount);
        meterRegistry.gauge("crawler.threads.platform", platformThreadCount);
        meterRegistry.gauge("crawler.db.connections.active", activeConnections);
        meterRegistry.gauge("crawler.db.connections.total", totalConnections);
    }

    private class BookCrawler implements Runnable {

        private final String url;
        private final PageProcessor pageProcessor;

        public BookCrawler(String url, PageProcessor pageProcessor) {
            this.url = url;
            this.pageProcessor = pageProcessor;
        }

        @Override
        public void run() {
            if (isRunning.get()) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Crawler was interrupted", e);
                    return;
                }
                HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
                Spider spider = Spider.create(pageProcessor)
                    .addUrl(url)
                    .setDownloader(httpClientDownloader);
                activeSpiders.putIfAbsent(url, spider);
                spider.run();
                activeSpiderCount.decrementAndGet();
                signalQueue();
            }
        }
    }

    private static void signalQueue() {
        QUEUE_LOCK.lock();
        try {
            QUEUE_NOT_EMPTY.signalAll();
        } finally {
            QUEUE_LOCK.unlock();
        }
    }
}
