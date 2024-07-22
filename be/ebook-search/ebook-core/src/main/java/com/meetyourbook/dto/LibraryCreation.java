package com.meetyourbook.dto;

import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.LibraryType;

public record LibraryCreation(String category, String press, String url) {


    public Library toEntity() {
        return Library.builder()
            .name(press)
            .type(LibraryType.findByDescription(category))
            .url(url)
            .build();
    }

}
