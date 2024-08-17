package com.meetyourbook.dto;

import lombok.Builder;

@Builder
public record LibraryUpdateInfo(

    String name,
    String category,
    String url

) {

}
