package com.meetyourbook.common.exception;

public class JsonParseException extends CrawlerException {

    public JsonParseException() {
        super("JSON 파일 형식이 올바르지 않습니다.");
    }
}
