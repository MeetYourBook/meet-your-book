package com.meetyourbook.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.dto.DuplicateLibrary;
import com.meetyourbook.dto.EbookPlatformCrawlRequest;
import com.meetyourbook.dto.LibraryCreationInfo;
import com.meetyourbook.dto.LibraryCreationResult;
import com.meetyourbook.entity.Library.EbookPlatform;
import com.meetyourbook.service.LibraryCrawlerService;
import com.meetyourbook.service.LibraryImportService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@WebMvcTest(LibraryCrawlerController.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class LibraryCrawlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryImportService libraryImportService;

    @MockBean
    private LibraryCrawlerService libraryCrawlerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Disabled
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

        when(libraryImportService.importLibrariesFromJson(any(MultipartFile.class))).thenReturn(any(
            LibraryCreationResult.class));

        // when & then
        mockMvc.perform(multipart("/admin/library-crawler/import").file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.importedCount").value(10))
            .andExpect(jsonPath("$.message").value("도서관 정보를 성공적으로 저장했습니다."));
    }

    @Test
    @DisplayName("파일 없이 요청 시 BAD REQUEST 를 반환한다.")
    void saveLibraryFromJsonWithoutFile() throws Exception {
        // when & then
        mockMvc.perform(multipart("/admin/library-crawler/import"))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertThat(
                result.getResolvedException() instanceof MissingServletRequestPartException).isTrue())
            .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo(
                "Required part 'file' is not present."));
    }

    @Test
    @DisplayName("플랫폼별 도서관 크롤링 요청 성공")
    void startCrawlingLibrary_success() throws Exception {
        // Given
        EbookPlatformCrawlRequest request = new EbookPlatformCrawlRequest("kyobo",
            "https://test.com");
        List<LibraryCreationInfo> infos = Arrays.asList(
            new LibraryCreationInfo("Library1", "url1", EbookPlatform.KYOBO),
            new LibraryCreationInfo("Library2", "url1", EbookPlatform.YES24),
            new LibraryCreationInfo("Library3", "url2", EbookPlatform.KYOBO)
        );

        LibraryCreationResult expectedResult = new LibraryCreationResult(2, 1,
            List.of(new DuplicateLibrary(infos.get(1), infos.get(0))));

        when(libraryCrawlerService.crawLibrary(any(EbookPlatformCrawlRequest.class)))
            .thenReturn(expectedResult);

        String requestBody = objectMapper.writeValueAsString(request);

        // When, Then
        mockMvc.perform(post("/admin/library-crawler/crawl-api")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.savedLibraryCount").value(2))
            .andExpect(jsonPath("$.duplicatedLibraryCount").value(1))
            .andExpect(jsonPath("$.duplicatedLibraries").isArray())
            .andExpect(jsonPath("$.duplicatedLibraries[0].duplicateLibrary.name").value("Library2"))
            .andExpect(jsonPath("$.duplicatedLibraries[0].duplicateSource.name").value("Library1"))
            .andDo(document("start-crawling-library",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("ebookPlatform").description("크롤링할 플랫폼"),
                    fieldWithPath("url").description("크롤링할 URL")
                ),
                responseFields(
                    fieldWithPath("savedLibraryCount").description("저장된 도서관 수"),
                    fieldWithPath("duplicatedLibraryCount").description("중복된 도서관 수"),
                    fieldWithPath("duplicatedLibraries").description("중복된 도서관 목록"),
                    fieldWithPath("duplicatedLibraries[].duplicateLibrary.name").description(
                        "중복된 도서관 이름"),
                    fieldWithPath("duplicatedLibraries[].duplicateLibrary.url").description(
                        "중복된 도서관 URL"),
                    fieldWithPath(
                        "duplicatedLibraries[].duplicateLibrary.ebookPlatform").description(
                        "중복된 도서관 전자책 플랫폼"),
                    fieldWithPath("duplicatedLibraries[].duplicateSource.name").description(
                        "중복의 원인이 된 도서관 이름"),
                    fieldWithPath("duplicatedLibraries[].duplicateSource.url").description(
                        "중복의 원인이 된 도서관 URL"),
                    fieldWithPath(
                        "duplicatedLibraries[].duplicateSource.ebookPlatform").description(
                        "중복의 원인이 된 도서관 전자책 플랫폼")
                )
            ));
    }

}