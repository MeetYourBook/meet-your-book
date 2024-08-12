package com.meetyourbook.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(NOT_FOUND.value())
            .message(ex.getMessage())
            .details(request.getDescription(false))
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
        MethodArgumentNotValidException ex, WebRequest request) {

        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
            .status(BAD_REQUEST.value())
            .message("Validation failed")
            .details(request.getDescription(false))
            .build();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorResponse.addValidationError(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
        WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(BAD_REQUEST.value())
            .message(ex.getMessage())
            .details(request.getDescription(false))
            .build();

        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
            .status(INTERNAL_SERVER_ERROR.value())
            .message(ex.getMessage())
            .details(request.getDescription(false))
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}