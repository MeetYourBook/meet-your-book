package com.meetyourbook.dto;

public record LibraryUpdateRequest(
    String name,
    String category,
    String url
) {

    public LibraryUpdateInfo toLibraryUpdateInfo() {
        return LibraryUpdateInfo.builder()
            .name(name)
            .category(category)
            .url(url)
            .build();
    }

}
