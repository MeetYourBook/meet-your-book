package com.meetyourbook.controller;


import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.dto.LibraryCreateRequest;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibraryUpdateRequest;
import com.meetyourbook.service.LibraryWebService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(LibraryController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibraryWebService libraryWebService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("도서관 목록 조회 요청 성공")
    void getLibraries_success() throws Exception {
        // Given
        Long libraryId1 = 1L;
        Long libraryId2 = 2L;

        LibraryResponse libraryResponse = new LibraryResponse(libraryId1, "도서관1");
        LibraryResponse libraryResponse2 = new LibraryResponse(libraryId2, "도서관2");

        when(libraryWebService.findAllLibraryResponses()).thenReturn(
            List.of(libraryResponse, libraryResponse2)
        );

        // When, Then
        mockMvc.perform(get("/api/libraries"))
            .andExpect(status().isOk())
            .andDo(document("libraries",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("[].id").description("도서관 ID"),
                    fieldWithPath("[].name").description("도서관 이름")
                )
            ));
    }

    @Test
    @DisplayName("도서관 단일 조회 요청 성공")
    void getLibrary_success() throws Exception {
        // Given
        Long libraryId1 = 1L;
        LibraryResponse libraryResponse = new LibraryResponse(libraryId1, "도서관1");
        when(libraryWebService.findById(libraryId1)).thenReturn(libraryResponse);

        // When, Then
        mockMvc.perform(get("/api/libraries/{id}", libraryId1))
            .andExpect(status().isOk())
            .andDo(document("library",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("id").description("도서관 ID")
                ),
                responseFields(
                    fieldWithPath("id").description("도서관 ID"),
                    fieldWithPath("name").description("도서관 이름")
                )
            ));

    }

    @Test
    @DisplayName("도서관 생성 요청 성공")
    void createLibrary() throws Exception {
        // Given
        LibraryCreateRequest libraryCreateRequest = new LibraryCreateRequest("대학교", "서울대학교",
            "https://test.com");
        String createRequest = objectMapper.writeValueAsString(libraryCreateRequest);

        // When, Then
        mockMvc.perform(post("/api/libraries")
                .contentType("application/json")
                .content(createRequest))
            .andExpect(status().isCreated())
            .andDo(document("create-library",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseHeaders(headerWithName("Location").description("생성된 도서관의 URL"))
            ));
    }

    @Test
    @DisplayName("도서관 수정 요청 성공")
    void updateLibrary_success() throws Exception {
        // Given
        LibraryUpdateRequest libraryUpdateRequest = new LibraryUpdateRequest("대학교", "서울대학교",
            "https://test.com");
        String updateRequest = objectMapper.writeValueAsString(libraryUpdateRequest);

        // When
        mockMvc.perform(patch("/api/libraries/{id}", 1L)
                .contentType("application/json")
                .content(updateRequest))
            .andExpect(status().isNoContent())
            .andDo(document("update-library",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("id").description("도서관 ID")),
                responseHeaders(headerWithName("Location").description("수정된 도서관의 URL"))
            ));

    }

    @Test
    @DisplayName("도서관 삭제 요청 성공")
    void deleteLibrary() throws Exception {
        // Given
        Long libraryId = 1L;

        // When, Then
        mockMvc.perform(delete("/api/libraries/{id}", libraryId))
            .andExpect(status().isNoContent())
            .andDo(document("delete-library",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("id").description("도서관 ID"))
            ));
    }
}