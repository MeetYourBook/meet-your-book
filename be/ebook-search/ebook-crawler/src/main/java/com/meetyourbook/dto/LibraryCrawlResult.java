package com.meetyourbook.dto;

import com.meetyourbook.entity.Library.EbookPlatform;

public record LibraryCrawlResult(

    String libraryCode,
    String libraryName,
    String libraryHost,
    String drmHost,
    String pushYn,
    String b2bType,
    String apiUrl

) {

    public LibraryCreationInfo toLibraryCreationInfo(EbookPlatform ebookPlatform) {
        return new LibraryCreationInfo(libraryName, libraryHost, ebookPlatform);
    }

}
