package com.meetyourbook.controller.admin;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.dto.BookCreateRequest;
import com.meetyourbook.dto.BookUpdateRequest;
import com.meetyourbook.service.BookService;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminBookController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class AdminBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;


    @Test
    @DisplayName("책 저장 요청 성공")
    void createBook_success() throws Exception {
        // Given
        BookCreateRequest bookCreateRequest = new BookCreateRequest("사피엔스", "유발 하라리", "김영사",
            LocalDate.of(2024, 1, 1), "https://test.com/image");
        String requestBody = objectMapper.writeValueAsString(bookCreateRequest);

        // When, Then
        mockMvc.perform(
                post("/admin/books")
                    .contentType("application/json")
                    .content(requestBody))
            .andExpect(status().isCreated())
            .andDo(document("create-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseHeaders(headerWithName("Location").description("생성된 책의 URL"))));
    }

    @Test
    @DisplayName("책 삭제 요청 성공")
    void deleteBook_success() throws Exception {
        // Given
        UUID bookId = UUID.randomUUID();

        // When, Then
        mockMvc.perform(delete("/admin/books/{bookId}", bookId))
            .andExpect(status().isNoContent())
            .andDo(document("delete-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("bookId").description("책 ID"))));
    }

    @Test
    @DisplayName("책 수정 요청 성공")
    void updateBook_success() throws Exception {
        // Given
        UUID bookId = UUID.randomUUID();
        BookUpdateRequest bookUpdateRequest = new BookUpdateRequest("사피엔스", "유발 하라리", "김영사", "교보문고",
            LocalDate.of(2024, 1, 1), "https://test.com/image_sapiens");
        String requestBody = objectMapper.writeValueAsString(bookUpdateRequest);

        // When, Then
        mockMvc.perform(
                patch("/admin/books/{bookId}", bookId)
                    .contentType("application/json")
                    .content(requestBody))
            .andExpect(status().isNoContent())
            .andDo(document("update-book",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(parameterWithName("bookId").description("책 ID")),
                responseHeaders(headerWithName("Location").description("수정된 책의 URL"))));
    }

}
