package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;
import java.util.Map;

public record BookCountVerificationResult(

    Long libraryId,
    String libraryName,
    int actualBookCount,
    int expectedBookCount,
    boolean isVerified

) {

    public static BookCountVerificationResult of(Library library,
        Map<Long, Integer> actualBookCounts) {
        int actualBookCount = actualBookCounts.getOrDefault(library.getId(), 0);
        int expectedBookCount = library.getTotalBookCount();

        return new BookCountVerificationResult(
            library.getId(),
            library.getName(),
            actualBookCount,
            expectedBookCount,
            actualBookCount == expectedBookCount
        );
    }

}
