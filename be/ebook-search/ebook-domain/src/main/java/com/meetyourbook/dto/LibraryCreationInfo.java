package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.LibraryType;
import lombok.Builder;

@Builder
public record LibraryCreationInfo(
    String category,
    String press,
    String url
) {

    public Library toEntity() {
        return Library.builder()
            .name(press)
            .type(LibraryType.findByDescription(category))
            .libraryUrl(url)
            .build();
    }

}
