package com.meetyourbook.dto;

import java.time.LocalDateTime;

public record CrawlerErrorResponse(

    String status,
    String message,
    LocalDateTime timestamp
) {

}
