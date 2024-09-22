package com.meetyourbook.dto;

public record LibraryCreateRequest(

    String name,
    String category,
    String url

) {

    public LibraryCreationInfo toLibraryCreationInfo() {
        return LibraryCreationInfo.builder()
            .name(name)
            .url(url)
            .build();
    }

}
