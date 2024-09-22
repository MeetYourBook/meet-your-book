package com.meetyourbook.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.meetyourbook.dto.LibraryPageResponse;
import com.meetyourbook.dto.LibraryResponse;
import com.meetyourbook.dto.LibrarySearchRequest;
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


    @Test
    @DisplayName("도서관 목록 페이징 조회 요청 성공")
    void getLibraries_withPagination_success() throws Exception {
        // Given
        String name = "도서관";
        int page = 0;
        int size = 10;

        LibrarySearchRequest request = new LibrarySearchRequest(name, page, size);
        List<LibraryResponse> content = List.of(
            new LibraryResponse(1L, "도서관1"),
            new LibraryResponse(2L, "도서관2")
        );
        LibraryPageResponse pageResponse = new LibraryPageResponse(page, size, 2, 1, content);

        when(libraryWebService.findLibraries(any(LibrarySearchRequest.class))).thenReturn(pageResponse);

        // When, Then
        mockMvc.perform(get("/libraries")
                .param("name", name)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size)))
            .andExpect(status().isOk())
            .andDo(document("libraries-search",
                queryParameters(
                    parameterWithName("name").description("도서관 이름 검색어").optional(),
                    parameterWithName("page").description("페이지 번호 (0부터 시작)").optional(),
                    parameterWithName("size").description("페이지 크기").optional()
                ),
                responseFields(
                    fieldWithPath("pageNumber").description("현재 페이지 번호"),
                    fieldWithPath("pageSize").description("페이지 크기"),
                    fieldWithPath("totalElements").description("전체 요소 수"),
                    fieldWithPath("totalPages").description("전체 페이지 수"),
                    fieldWithPath("content").description("도서관 목록"),
                    fieldWithPath("content[].id").description("도서관 ID"),
                    fieldWithPath("content[].name").description("도서관 이름")
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
        mockMvc.perform(get("/libraries/{id}", libraryId1))
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
}