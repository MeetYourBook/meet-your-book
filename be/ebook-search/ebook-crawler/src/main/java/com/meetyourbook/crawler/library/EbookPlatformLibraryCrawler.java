package com.meetyourbook.crawler.library;

import com.meetyourbook.dto.LibraryCrawlerTarget;
import com.meetyourbook.dto.LibraryCreationInfo;
import java.util.List;

public interface EbookPlatformLibraryCrawler {

    List<LibraryCreationInfo> crawlLibrary(LibraryCrawlerTarget target);
}
