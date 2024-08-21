package com.meetyourbook.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.meetyourbook.dto.BookCountVerificationResult;
import com.meetyourbook.service.BookCountVerificationService;
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

@WebMvcTest(BookVerificationController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class BookVerificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCountVerificationService bookCountVerificationService;


    @Test
    @DisplayName("책 수량 검증 요청 성공")
    void verifyBookCount_success() throws Exception {
        // Given
        BookCountVerificationResult bookCountVerificationResult = new BookCountVerificationResult(
            1L, "도서관1", 10, 10, true);
        when(bookCountVerificationService.checkBookCount()).thenReturn(
            List.of(bookCountVerificationResult));

        // When, Then
        mockMvc.perform(get("/api/admin/verify-book-count"))
            .andExpect(status().isOk())
            .andDo(document("verify-book-count",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("[].libraryId").description("도서관 ID"),
                    fieldWithPath("[].libraryName").description("도서관 이름"),
                    fieldWithPath("[].actualBookCount").description("크롤링한 책 수"),
                    fieldWithPath("[].expectedBookCount").description("도서관에 등록된 책 수"),
                    fieldWithPath("[].isVerified").description("책 수량 일치 여부")
                )
            ));

    }
}