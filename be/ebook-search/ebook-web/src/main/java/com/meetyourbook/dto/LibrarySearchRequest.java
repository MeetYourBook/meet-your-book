package com.meetyourbook.dto;

public record LibrarySearchRequest(

    String name,
    Integer page,
    Integer size

) {

    public LibrarySearchCondition toLibrarySearchCondition() {
        return LibrarySearchCondition.from(name, page, size);
    }

}
