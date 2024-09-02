package com.meetyourbook.dto;

public record LibraryCreateRequest(

    String category,
    String press,
    String url

) {

    public LibraryCreationInfo toLibraryCreationInfo() {
        return LibraryCreationInfo.builder()
            .name(press)
            .url(url)
            .build();
    }

}
