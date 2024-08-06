package com.meetyourbook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record CrawlerErrorResponse(

    @Schema(description = "HTTP 상태 코드", example = "500")
    String status,

    @Schema(description = "에러 메시지", example = "크롤러가 시작되지 않았습니다.")
    String message,

    @Schema(description = "에러 발생 시각", example = "2024-01-01T00:00:00")
    LocalDateTime timestamp
) {

}
