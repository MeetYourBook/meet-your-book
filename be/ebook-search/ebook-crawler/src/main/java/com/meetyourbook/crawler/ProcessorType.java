package com.meetyourbook.crawler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public enum ProcessorType {

    BOOK("BookPage"),
    BOOK_COUNT("BookCount");

    private final String beanName;

    ProcessorType(String beanName) {
        this.beanName = beanName;
    }

    public static ProcessorType fromString(String processorName) {
        for (ProcessorType type : ProcessorType.values()) {
            if (type.getBeanName().equalsIgnoreCase(processorName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("알 수 없는 크롤링 프로세서 이름입니다: " + processorName);
    }
}
