package com.meetyourbook.service;

import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.util.JsonConvertor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class LibraryImportService {

    private final LibraryDomainService libraryDomainService;
    private final JsonConvertor jsonConvertor;

    public int importLibrariesFromJson(MultipartFile file) {
        List<LibraryCreationInfo> libraryCreationInfos = jsonConvertor.parseJsonFile(file,
            LibraryCreationInfo.class);

        return libraryDomainService.createLibraries(libraryCreationInfos);
    }

}
