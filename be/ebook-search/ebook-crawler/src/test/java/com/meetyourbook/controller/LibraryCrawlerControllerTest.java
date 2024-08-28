package com.meetyourbook.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.meetyourbook.service.LibraryCrawlerService;
import com.meetyourbook.service.LibraryImportService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@WebMvcTest(LibraryCrawlerController.class)
class LibraryCrawlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryImportService libraryImportService;

    @MockBean
    private LibraryCrawlerService libraryCrawlerService;

    @Test
    @DisplayName("JSON 파일 업로드 시 저장한 도서관 수를 반환한다.")
    void saveLibraryFromJson() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test-libraries.json",
            MediaType.APPLICATION_JSON_VALUE,
            "{\"test\": \"data\"}".getBytes()
        );

        when(libraryImportService.importLibrariesFromJson(any(MultipartFile.class))).thenReturn(10);

        // when & then
        mockMvc.perform(multipart("/api/library-crawler/import").file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(10))
            .andExpect(jsonPath("$.message").value("도서관 정보를 성공적으로 저장했습니다."));
    }

    @Test
    @DisplayName("파일 없이 요청 시 BAD REQUEST 를 반환한다.")
    void saveLibraryFromJsonWithoutFile() throws Exception {
        // when & then
        mockMvc.perform(multipart("/api/library-crawler/import"))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(
                result.getResolvedException() instanceof MissingServletRequestPartException).isTrue())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(
                "Required part 'file' is not present."));
    }

}