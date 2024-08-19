package com.meetyourbook.common.exception;

import com.meetyourbook.dto.CrawlerErrorResponse;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Slf4j
@RestControllerAdvice
public class CrawlerExceptionHandler {

    @ExceptionHandler(CrawlerAlreadyRunningException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<CrawlerErrorResponse> handleCrawlerAlreadyRunningException(
        CrawlerAlreadyRunningException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new CrawlerErrorResponse(
                HttpStatus.CONFLICT.toString(),
                e.getMessage(),
                LocalDateTime.now()
            ));
    }

    @ExceptionHandler(CrawlerNotRunningException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<CrawlerErrorResponse> handleCrawlerNotRunningException(
        CrawlerNotRunningException e) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new CrawlerErrorResponse(
                HttpStatus.CONFLICT.toString(),
                e.getMessage(),
                LocalDateTime.now()
            ));
    }

    @ExceptionHandler(JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CrawlerErrorResponse> handleJsonParseException(
        CrawlerNotRunningException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new CrawlerErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(),
                LocalDateTime.now()));
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<CrawlerErrorResponse> handleMissingServletRequestPartException(
        CrawlerNotRunningException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new CrawlerErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage(),
                LocalDateTime.now()));
    }

}
