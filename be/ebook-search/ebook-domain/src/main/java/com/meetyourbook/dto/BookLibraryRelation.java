package com.meetyourbook.dto;

import java.util.UUID;

public record BookLibraryRelation(

    UUID bookId,
    Long libraryId,
    String bookUrl

) {

}
