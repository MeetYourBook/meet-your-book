package com.meetyourbook.crawler;

import com.meetyourbook.entity.Library;
import com.meetyourbook.service.LibraryService;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Spider.Status;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookCrawlerRunner implements CommandLineRunner {

    private final BookPageProcessor bookPageProcessor;
    private static Spider spider;
    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
    private final AtomicInteger processedPageCount = new AtomicInteger(0);
    private long startTime;
    private static final String REDIS_HOST = "localhost";
    private final LibraryService libraryService;
    private final DataSource dataSource;
    private final MeterRegistry meterRegistry;
    private static final int MAX_PAGES_TO_SEARCH = 2000; // 크롤링할 최대 페이지 수
    public static final ReentrantLock QUEUE_LOCK = new ReentrantLock();
    public static final Condition QUEUE_NOT_EMPTY = QUEUE_LOCK.newCondition();

    @Override
    public void run(String... args) throws Exception {
        List<Library> libraries = libraryService.findAll();

        Queue<String> pageQueue = new ConcurrentLinkedQueue<>(
            libraries.stream()
                .filter(Library::hasMainInk)
                .map(library -> library.getUrlWithQueryParameters(500))
                .limit(MAX_PAGES_TO_SEARCH)
                .toList()
        );

        AtomicInteger pageCount = new AtomicInteger(0);
        ConcurrentMap<String, Boolean> visited = new ConcurrentHashMap<>();

        // Virtual Thread를 사용하려면 excutor 변경
        startResourceMonitoring();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            while (pageCount.get() < MAX_PAGES_TO_SEARCH) {
                QUEUE_LOCK.lock();
                try {
                    while (pageQueue.isEmpty()) {
                        QUEUE_NOT_EMPTY.await();
                    }
                    String url = pageQueue.poll();
                    if (url != null && !visited.containsKey(url)) {
                        executor.submit(new BookCrawler(pageCount, visited, url, pageQueue));
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
        private final Queue<String> pageQueue;

        public BookCrawler(AtomicInteger count, ConcurrentMap<String, Boolean> visited, String url, Queue<String> pageQueue) {
            this.count = count;
            this.visited = visited;
            this.url = url;
            this.pageQueue = pageQueue;
        }

        @Override
        public void run() {
            if (!visited.containsKey(url) && count.get() < MAX_PAGES_TO_SEARCH) {
                Spider spider = Spider.create(bookPageProcessor)
                    .addUrl(url);

                spider.run();

                visited.put(url, true);
                count.incrementAndGet();

                try {
                    String s = fetchNextUrl(url);
                    pageQueue.offer(s);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

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
        scheduler.scheduleAtFixedRate(this::logResourceUsage, 1, 1, TimeUnit.SECONDS);

    }

    private void logResourceUsage() {
        // 스레드 리소스 모니터링
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

    private void startTpsMonitoring() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::logTps, 1, 1, TimeUnit.SECONDS);
    }

    private void logTps() {
        long currentTime = System.currentTimeMillis();
        long elapsedSeconds = (currentTime - startTime) / 1000;
        if (elapsedSeconds > 0) {
            double tps = (double) processedPageCount.get() / elapsedSeconds;
            log.info("Current TPS: {}", String.format("%.2f", tps));
        }
    }

    private void startMonitoringThread() {
        new Thread(() -> {
            while (!spider.getStatus().equals(Status.Stopped)) {
                int activeThreadCount = spider.getThreadAlive();
                log.info("Active thread count: {}", activeThreadCount);

                Thread[] threads = new Thread[Thread.activeCount()];
                Thread.enumerate(threads);

                int virtualThreadCnt = 0;
                int platformThreadCnt = 0;

                for (Thread t : threads) {
                    log.info("thread name: {}", t.getName());
                    if (t.isVirtual()) {
                        virtualThreadCnt++;
                    } else {
                        platformThreadCnt++;
                    }
                }
                log.info("Virtual thread count: {}", virtualThreadCnt);
                log.info("Platform thread count: {}", platformThreadCnt);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error("Monitoring thread interrupted", e);
                }
            }
        }).start();
    }

    private String fetchNextUrl(String url) throws URISyntaxException {
        URI uri = URI.create(url);
        String query = uri.getQuery();
        String[] params = query.split("&");
        StringBuilder newQuery = new StringBuilder();

        for (String param : params) {
            if (param.startsWith("pageIndex=")) {
                String[] keyValue = param.split("=");
                int pageIndex = Integer.parseInt(keyValue[1]);
                pageIndex++;
                newQuery.append("pageIndex=").append(pageIndex).append("&");
            } else {
                newQuery.append(param).append("&");
            }
        }

        if (!newQuery.isEmpty()) {
            newQuery.setLength(newQuery.length() - 1);
        }

        URI newUri = new URI(
            uri.getScheme(),
            uri.getAuthority(),
            uri.getPath(),
            newQuery.toString(),
            uri.getFragment()
        );
        log.info("newUri: {}", newUri);

        return newUri.toString();
    }


}
