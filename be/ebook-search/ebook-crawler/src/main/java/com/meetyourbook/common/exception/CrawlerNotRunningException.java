package com.meetyourbook.common.exception;

public class CrawlerNotRunningException extends CrawlerException{
    public CrawlerNotRunningException() {
        super("현재 크롤러가 실행중이지 않습니다.");
    }
}
