package com.meetyourbook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CrawlerRequest(

    @Schema(description = "크롤링할 프로세서 이름", example = "BookPageProcessor")
    @NotBlank(message = "프로세서 이름은 필수입니다.")
    String processor,

    @Schema(description = "처음에 크롤링할 최대 URL 수", example = "500", maximum = "1954")
    @Max(value = 1954, message = "최대 URL 수는 1954개 이하로 설정해야 합니다.")
    @Min(value = 1, message = "최대 URL 수는 1개 이상으로 설정해야 합니다.")
    int initMaxUrl,

    @Schema(description = "한 페이지당 크롤링 할 책 수", example = "10", maximum = "99")
    @Max(value = 99, message = "한 페이지당 크롤링 할 책 수는 99개 이하로 설정해야 합니다.")
    int viewCount) {

}
