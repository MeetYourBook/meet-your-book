package com.meetyourbook.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationErrorResponse {

    private final int status;
    private final String message;
    private final String details;
    private final LocalDateTime timestamp;
    private final Map<String, String> errors;

    @Builder
    public ValidationErrorResponse(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
        this.errors = new HashMap<>();
    }

    public void addValidationError(String field, String errorMessage) {
        errors.put(field, errorMessage);
    }

}
