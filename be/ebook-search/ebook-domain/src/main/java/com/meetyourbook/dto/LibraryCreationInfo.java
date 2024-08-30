package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;
import lombok.Builder;

@Builder
public record LibraryCreationInfo(
    String press,
    String url
) {

    public Library toEntity() {
        return Library.builder()
            .name(press)
            .libraryUrl(url)
            .build();
    }

}
