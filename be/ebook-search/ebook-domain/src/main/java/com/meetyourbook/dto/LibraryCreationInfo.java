package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.EbookPlatform;
import lombok.Builder;

@Builder
public record LibraryCreationInfo(
    String name,
    String url,
    EbookPlatform ebookPlatform
) {

    public Library toEntity() {
        return Library.builder()
            .name(name)
            .libraryUrl(url)
            .ebookPlatform(ebookPlatform)
            .build();
    }

}
