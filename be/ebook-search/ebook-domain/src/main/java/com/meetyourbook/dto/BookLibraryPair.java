package com.meetyourbook.dto;

import java.util.UUID;

public record BookLibraryPair(

    UUID bookId,
    Long libraryId,
    String bookUrl

) {

}
