package com.meetyourbook.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.dto.LibraryCreationInfo;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonConvertor {

    private final ObjectMapper objectMapper;

    public List<LibraryCreationInfo> readFromJson(String filePath) {
        try {
            List<LibraryCreationInfo> libraryCreationInfos = readLibraryCreationsFromJson(filePath);
            log.info("Successfully read {} libraries", libraryCreationInfos.size());
            return libraryCreationInfos;
        } catch (IOException e) {
            log.error("Error reading JSON file: {}", filePath, e);
            throw new RuntimeException("Failed to read library data from JSON", e);
        }
    }

    private List<LibraryCreationInfo> readLibraryCreationsFromJson(String filePath)
        throws IOException {

        return objectMapper.readValue(new File(filePath), new TypeReference<>() {});
    }


}
