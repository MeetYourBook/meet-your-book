package com.meetyourbook.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CrawlerRequest(

    @NotBlank(message = "프로세서 이름은 필수입니다.")
    String processor,

    @Max(value = 99, message = "한 페이지당 크롤링 할 책 수는 99개 이하로 설정해야 합니다.")
    int viewCount,

    @Min(value = 1, message = "시작 도서관 ID는 1 이상이어야 합니다.")
    Long startLibraryId,

    Long endLibraryId
) {

}
