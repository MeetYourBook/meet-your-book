package com.meetyourbook.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.entity.Library;
import com.meetyourbook.entity.Library.LibraryType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class LibraryImportServiceTest {

    @Autowired
    private LibraryImportService libraryImportService;

    @MockBean
    private LibraryDomainService libraryDomainService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("JSON 파일을 읽어 저장한 도서관 수를 반환한다.")
    void testImportLibrariesFromJson() throws IOException {
        // given
        List<Library> testLibraries = generateTestLibraries(20); // 테스트 데이터 생성
        String jsonContent = objectMapper.writeValueAsString(testLibraries);
        MockMultipartFile jsonFile = new MockMultipartFile("file", "test-libraries.json",
            "application/json", jsonContent.getBytes());
        when(libraryDomainService.createLibraries(any())).thenReturn(testLibraries.size());

        // when
        int importedCount = libraryImportService.importLibrariesFromJson(jsonFile);

        // then
        assertThat(importedCount).isEqualTo(testLibraries.size());
    }

    private List<Library> generateTestLibraries(int count) {
        List<Library> libraries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            libraries.add(createDummyLibrary());
        }

        return libraries;
    }

    private Library createDummyLibrary() {
        UUID uuid = UUID.randomUUID();
        return Library.builder()
            .name("도서관 " + uuid.toString().substring(0, 8))
            .libraryUrl("http://library" + uuid.toString().substring(0, 8) + ".com")
            .totalBookCount((int) (Math.random() * 10000) + 1000)
            .type(LibraryType.CORPORATE_LIBRARY)
            .build();
    }


}