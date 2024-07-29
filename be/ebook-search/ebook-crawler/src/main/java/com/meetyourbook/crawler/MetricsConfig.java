package com.meetyourbook.crawler;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean(name = "pagesCounter")
    public Counter processedPagesCounter(MeterRegistry meterRegistry) {
        return Counter.builder("processed_pages")
            .description("성공적으로 처리되고 저장된 페이지 수")
            .register(meterRegistry);
    }

    @Bean(name = "booksCounter")
    public Counter processedBooksCounter(MeterRegistry meterRegistry) {
        return Counter.builder("processed_books")
            .description("성공적으로 처리되고 저장된 책 수")
            .register(meterRegistry);
    }
}