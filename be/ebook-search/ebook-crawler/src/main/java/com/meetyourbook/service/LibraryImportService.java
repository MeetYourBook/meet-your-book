package com.meetyourbook.service;

import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.util.JsonConvertor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryImportService {

    private final LibraryDomainService libraryService;
    private final JsonConvertor jsonConvertor;

    public void saveLibraryFromJson(String path) {
        List<LibraryCreationInfo> libraryCreationInfos = jsonConvertor.readFromJson(path);
        libraryService.createLibraries(libraryCreationInfos);
    }

}
