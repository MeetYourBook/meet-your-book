package com.meetyourbook.dto;


import java.util.List;

public record LibraryCreationResult(
    int savedLibraryCount,
    int duplicatedLibraryCount,
    List<DuplicateLibrary> duplicatedLibraries

) {

}
