package com.meetyourbook.common.exception;

public abstract class CrawlerException extends RuntimeException {
    public CrawlerException(String message) {
        super(message);
    }
}
