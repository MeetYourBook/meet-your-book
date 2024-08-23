package com.meetyourbook.dto;

public record LibraryCreateRequest(

    String category,
    String press,
    String url

) {

    public LibraryCreationInfo toLibraryCreationInfo() {
        return LibraryCreationInfo.builder()
            .category(category)
            .press(press)
            .url(url)
            .build();
    }

}
