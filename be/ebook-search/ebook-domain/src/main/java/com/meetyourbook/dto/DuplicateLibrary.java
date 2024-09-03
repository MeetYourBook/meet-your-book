package com.meetyourbook.dto;

public record DuplicateLibrary(

    LibraryCreationInfo duplicateLibrary,
    LibraryCreationInfo duplicateSource
) {

}
