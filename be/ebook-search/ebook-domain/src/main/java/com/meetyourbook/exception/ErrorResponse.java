package com.meetyourbook.exception;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int status;
    private final String message;
    private final String details;
    private final LocalDateTime timestamp;

    @Builder
    public ErrorResponse(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }
}