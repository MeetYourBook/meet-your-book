package com.meetyourbook.crawler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.service.BookCrawlerService;
import com.meetyourbook.service.BookQueueService;
import io.micrometer.core.instrument.Counter;
import java.time.LocalDate;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

@ExtendWith(MockitoExtension.class)
public class BookPageProcessorTest {

    @Mock
    private BookQueueService bookQueueService;
    @Mock
    private Counter pagesCounter;
    @Mock
    private Counter booksCounter;
    @Mock
    private Page page;

    @Captor
    private ArgumentCaptor<List<BookInfo>> bookInfosCaptor;

    private BookPageProcessor bookPageProcessor;

    @BeforeEach
    void setUp() {
        bookPageProcessor = new BookPageProcessor(bookQueueService, pagesCounter, booksCounter);
        BookCrawlerService.pageQueue.clear();
    }


    @Test
    @DisplayName("옳바른 HTML 문서를 파싱하게 되면 BookInfo를 큐에 추가한다.")
    void parseBookInfo_ShouldAddToQueue() {
        // given
        String htmlContent = """
            <div class="book_resultList">
                <div>
                    <div class="store">TestStore</div>
                    <div class="tit">테스트 책 제목</div>
                    <div class="writer">
                        제이든
                        <span>테스트 출판사</span>
                        2024-01-01
                    </div>
                    <img src="//example.com/book-cover.jpg" />
                </div>
            </div>
            """;
        setupPage(htmlContent);

        when(page.getUrl()).thenReturn(new PlainText(
            "https://example.com/content/contentList.ink?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=20&pageIndex=1&recordCount=20"));

        //when
        bookPageProcessor.process(page);

        //then
        verify(bookQueueService).addBookInfosToQueue(bookInfosCaptor.capture());
        List<BookInfo> capturedBookInfos = bookInfosCaptor.getValue();
        assertThat(1).isEqualTo(capturedBookInfos.size());
        BookInfo bookInfo = capturedBookInfos.getFirst();
        assertThat(bookInfo.getTitle()).isEqualTo("테스트 책 제목");
        assertThat(bookInfo.getAuthor()).isEqualTo("제이든");
        assertThat(bookInfo.getPublisher()).isEqualTo("테스트 출판사");
        assertThat(bookInfo.getImageUrl()).isEqualTo("example.com/book-cover.jpg");
        assertThat(bookInfo.getPublishDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(bookInfo.getBaseUrl()).isEqualTo("https://example.com");

        verify(booksCounter).increment(1);
        verify(pagesCounter).increment();
    }

    @Test
    @DisplayName("책 목록이 없어서 파싱 결과가 없으면 큐에 추가하지 않는다")
    void parseEmptyList_ShouldNotAddToQueue() {
        // Given
        String htmlContent = "<div class='book_resultList'></div>";
        setupPage(htmlContent);
        when(page.getUrl()).thenReturn(new PlainText(
            "https://example.com/content/contentList.ink?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=20&pageIndex=1&recordCount=20"));

        // When
        bookPageProcessor.process(page);

        // Then
        verify(bookQueueService, never()).addBookInfosToQueue(any());
        verify(booksCounter, never()).increment(anyLong());
        verify(pagesCounter, never()).increment();
    }

    @Test
    @DisplayName("잘못된 날짜 정보를 파싱할 경우 null값을 저장한다.")
    void parseInvalidDateFormat_ShouldSaveNull() {
        // Given
        String htmlContent = """
            <div class="book_resultList">
                <div>
                    <div class="store">TestStore</div>
                    <div class="tit">테스트 책 제목</div>
                    <div class="writer">
                        제이든
                        <span>테스트 출판사</span>
                        2024-0101
                    </div>
                    <img src="//example.com/book-cover.jpg" />
                </div>
            </div>
            """;
        setupPage(htmlContent);
        when(page.getUrl()).thenReturn(new PlainText(
            "https://example.com/content/contentList.ink?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=20&pageIndex=1&recordCount=20"));

        // When
        bookPageProcessor.process(page);

        // Then
        verify(bookQueueService).addBookInfosToQueue(bookInfosCaptor.capture());
        List<BookInfo> capturedBookInfos = bookInfosCaptor.getValue();
        assertThat(1).isEqualTo(capturedBookInfos.size());
        BookInfo bookInfo = capturedBookInfos.getFirst();
        assertThat(bookInfo.getPublishDate()).isNull();
    }

    @Test
    @DisplayName("ResultList Element를 찾지 못한 경우 queue에 파싱 결과를 추가하지 않는다.")
    void parseEmptyResultList_ShouldNotAddToQueue() {
        // Given
        String htmlContent = "<div></div>";
        setupPage(htmlContent);
        when(page.getUrl()).thenReturn(new PlainText(
            "https://example.com/content/contentList.ink?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=20&pageIndex=1&recordCount=20"));

        // When
        bookPageProcessor.process(page);

        // Then
        verify(bookQueueService, never()).addBookInfosToQueue(any());
        verify(booksCounter, never()).increment(anyLong());
        verify(pagesCounter, never()).increment();
    }


    private void setupPage(String html) {
        Document doc = Jsoup.parse(html);
        when(page.getHtml()).thenReturn(new Html(doc));
    }

}
