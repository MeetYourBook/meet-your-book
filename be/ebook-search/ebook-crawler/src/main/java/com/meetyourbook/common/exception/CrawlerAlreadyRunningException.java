package com.meetyourbook.common.exception;

public class CrawlerAlreadyRunningException extends CrawlerException{
    public CrawlerAlreadyRunningException() {
        super("크롤러가 이미 실행중입니다.");
    }
}
