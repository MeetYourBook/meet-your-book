package com.meetyourbook.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.common.exception.CrawlerAlreadyRunningException;
import com.meetyourbook.common.exception.CrawlerNotRunningException;
import com.meetyourbook.dto.CrawlerRequest;
import com.meetyourbook.service.BookCrawlerService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BookCrawlerController.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
class BookCrawlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCrawlerService bookCrawlerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("크롤러 실행 요청 성공")
    void startCrawler_success() throws Exception {
        // Given
        String crawlerId = UUID.randomUUID().toString();
        CrawlerRequest request = new CrawlerRequest("BookPage", 99, 1L, 20L);
        String requestBody = objectMapper.writeValueAsString(request);
        when(bookCrawlerService.startCrawl(anyString(), anyInt(), anyLong(), anyLong())).thenReturn(
            crawlerId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/crawler/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(crawlerId))
            .andExpect(jsonPath("$.message").exists())
            .andDo(document("start-crawler",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("processor").description("크롤러 프로세서 이름"),
                    fieldWithPath("viewCount").description("조회할 도서 수"),
                    fieldWithPath("startLibraryId").description("시작 도서관 ID"),
                    fieldWithPath("endLibraryId").description("마지막 도서관 ID")
                ),
                responseFields(
                    fieldWithPath("id").description("크롤러 ID"),
                    fieldWithPath("message").description("응답 메시지")
                )));
    }

    @Test
    @DisplayName("크롤러를 중복 실행 하는 경우 에러가 발생하는지 확인")
    void startCrawlerWhenAlreadyRunning_returnConflict() throws Exception {
        // Given
        CrawlerRequest request = new CrawlerRequest("BookPage", 99, 1L, 20L);
        String requestBody = objectMapper.writeValueAsString(request);
        when(bookCrawlerService.startCrawl(anyString(), anyInt(), anyLong(), anyLong())).thenThrow(
            new CrawlerAlreadyRunningException());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/crawler/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(jsonPath("$.status").value("409 CONFLICT"))
            .andExpect(jsonPath("$.message").value("크롤러가 이미 실행중입니다."))
            .andExpect(jsonPath("$.timestamp").exists())
            .andDo(document("start-crawler-already-running",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("processor").description("크롤러 프로세서 이름"),
                    fieldWithPath("viewCount").description("조회할 도서 수"),
                    fieldWithPath("startLibraryId").description("시작 도서관 ID"),
                    fieldWithPath("endLibraryId").description("마지막 도서관 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태 코드와 설명"),
                    fieldWithPath("message").description("에러 메시지"),
                    fieldWithPath("timestamp").description("에러 발생 시각")
                )));
    }

    @Test
    @DisplayName("크롤러 중지 요청 성공")
    void stopCrawler_success() throws Exception {
        // Given
        doNothing().when(bookCrawlerService).stopCrawl();
        String crawlerId = UUID.randomUUID().toString();
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/crawler/{id}/stop", crawlerId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(crawlerId))
            .andExpect(jsonPath("$.message").exists())
            .andDo(document("stop-crawler",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("id").description("크롤러 ID"),
                    fieldWithPath("message").description("응답 메시지")
                )));

    }

    @Test
    @DisplayName("실행중인 크롤러가 없을 떄 중지 요청 시 실패")
    void stopCrawlerWhenNotRunning_returnConflict() throws Exception {
        // Given
        String crawlerId = UUID.randomUUID().toString();
        doThrow(new CrawlerNotRunningException()).when(bookCrawlerService).stopCrawl();

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/api/crawler/{id}/stop", crawlerId))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.toString()))
            .andExpect(jsonPath("$.message").value("현재 크롤러가 실행중이지 않습니다."))
            .andExpect(jsonPath("$.timestamp").exists())
            .andDo(document("stop-crawler-not-found",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태 코드와 설명"),
                    fieldWithPath("message").description("에러 메시지"),
                    fieldWithPath("timestamp").description("에러 발생 시각")
                )));

    }


}