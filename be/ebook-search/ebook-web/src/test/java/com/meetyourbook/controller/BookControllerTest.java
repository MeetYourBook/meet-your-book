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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.meetyourbook.dto.BookLibraryResponse;
import com.meetyourbook.dto.BookPageResponse;
import com.meetyourbook.dto.BookSearchRequest;
import com.meetyourbook.dto.SimpleBookResponse;
import com.meetyourbook.service.BookService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
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

@WebMvcTest(BookController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("책 검색 요청 성공")
    void searchBooks_success() throws Exception {
        // Given
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        Long libraryId1 = new Random().nextLong(100);
        Long libraryId2 = new Random().nextLong(100);

        BookLibraryResponse bookLibraryResponse1 = BookLibraryResponse.builder()
            .id(libraryId1)
            .libraryName("Library1")
            .bookLibraryUrl("https://library1.com/book1")
            .build();

        BookLibraryResponse bookLibraryResponse2 = BookLibraryResponse.builder()
            .id(libraryId2)
            .libraryName("Library2")
            .bookLibraryUrl("https://library2.com/book1")
            .build();

        BookLibraryResponse bookLibraryResponse3 = BookLibraryResponse.builder()
            .id(libraryId1)
            .libraryName("Library1")
            .bookLibraryUrl("https://library1.com/book2")
            .build();

        SimpleBookResponse simpleBookResponse1 = SimpleBookResponse.builder()
            .id(bookId1)
            .title("Book1")
            .author("Author1")
            .publisher("출판사1")
            .imageUrl("https://book1.com/image")
            .libraryResponses(Arrays.asList(bookLibraryResponse1, bookLibraryResponse2))
            .build();

        SimpleBookResponse simpleBookResponse2 = SimpleBookResponse.builder()
            .id(bookId2)
            .title("Book2")
            .author("Author2")
            .publisher("출판사2")
            .imageUrl("https://book2.com/image")
            .libraryResponses(Collections.singletonList(bookLibraryResponse3))
            .build();

        BookPageResponse bookPageResponse = BookPageResponse.builder()
            .pageNumber(0)
            .totalPages(1)
            .totalElements(2)
            .content(Arrays.asList(simpleBookResponse1, simpleBookResponse2))
            .build();

        BookSearchRequest bookSearchRequest = new BookSearchRequest("Book1", "Author1", "출판사1",
            Arrays.asList(libraryId1, libraryId2), 0, 10, "title");

        when(bookService.searchBooks(any())).thenReturn(bookPageResponse);

        // When, Then
        mockMvc.perform(
                get("/books")
                    .param("title", bookSearchRequest.title())
                    .param("author", bookSearchRequest.author())
                    .param("publisher", bookSearchRequest.publisher())
                    .param("libraries", String.valueOf(libraryId1))
                    .param("page", "0")
                    .param("size", "10")
                    .param("sort", "title"))
            .andExpect(status().isOk())
            .andDo(document("search-books",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(parameterWithName("title").optional().description("제목"),
                    parameterWithName("author").optional().description("저자"),
                    parameterWithName("publisher").optional().description("출판사"),
                    parameterWithName("libraries").description("도서관 필터링을 위한 ID").optional(),
                    parameterWithName("page").description("검색 페이지 인덱스").optional(),
                    parameterWithName("size").description("페이지 사이즈").optional(),
                    parameterWithName("sort").description("정렬 기준").optional()),
                responseFields(fieldWithPath("pageNumber").description("현재 페이지 인덱스"),
                    fieldWithPath("pageSize").description("페이지 사이즈"),
                    fieldWithPath("totalElements").description("총 책 권수"),
                    fieldWithPath("totalPages").description("총 페이지 수"),
                    fieldWithPath("content[].id").description("책 ID"),
                    fieldWithPath("content[].title").description("책 제목"),
                    fieldWithPath("content[].author").description("책 저자"),
                    fieldWithPath("content[].publisher").description("책 출판사"),
                    fieldWithPath("content[].imageUrl").description("책 이미지 URL"),
                    fieldWithPath("content[].libraryResponses[].id").description("도서관 ID"),
                    fieldWithPath("content[].libraryResponses[].libraryName").description("도서관 이름"),
                    fieldWithPath("content[].libraryResponses[].bookLibraryUrl").description(
                        "책 상세 페이지 주소"))));

    }

}
