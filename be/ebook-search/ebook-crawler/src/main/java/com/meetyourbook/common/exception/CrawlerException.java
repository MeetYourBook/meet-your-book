package com.meetyourbook.common.exception;

public abstract class CrawlerException extends RuntimeException {

    protected CrawlerException(String message) {
        super(message);
    }
}
