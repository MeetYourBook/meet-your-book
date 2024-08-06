package com.meetyourbook.common.exception;

import com.meetyourbook.dto.CrawlerErrorResponse;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CrawlerErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument error: ", e);
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new CrawlerErrorResponse(
                HttpStatus.BAD_REQUEST.toString(),
                e.getMessage(),
                LocalDateTime.now()
            ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CrawlerErrorResponse> handleIllegalStateException(IllegalStateException e) {
        log.error("Illegal state error: ", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new CrawlerErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                e.getMessage(),
                LocalDateTime.now()
            ));
    }

}
