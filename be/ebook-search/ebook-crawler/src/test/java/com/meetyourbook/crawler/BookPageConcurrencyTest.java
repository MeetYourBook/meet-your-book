package com.meetyourbook.crawler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.meetyourbook.dto.BookInfo;
import com.meetyourbook.service.BookQueueService;
import io.micrometer.core.instrument.Counter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
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
class BookPageConcurrencyTest {

    @Mock
    private BookQueueService bookQueueService;

    @Mock
    private Counter pagesCounter;

    @Mock
    private Counter booksCounter;

    @Captor
    private ArgumentCaptor<List<BookInfo>> bookInfosCaptor;

    private BookPageProcessor bookPageProcessor;

    @BeforeEach
    void setUp() {
        bookPageProcessor = new BookPageProcessor(bookQueueService, pagesCounter, booksCounter);
    }

    @Test
    @DisplayName("BookPageProcesso가 싱글톤인 상황에서 여러 스레드가 동시에 접근해도 url 저장이 문제되지 않는다.")
    void testBookPageProcessorConcurrency_whenSaveUrl() {
        // Given
        int threadCnt = 1000;
        CountDownLatch latch = new CountDownLatch(threadCnt);
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < threadCnt; i++) {
                String url = "http://test" + i + ".com/main.ink"
                    + "?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=20&pageIndex=1&recordCount=20";
                executor.submit(() -> {
                    try {
                        Page page = createMockPage(url);
                        bookPageProcessor.process(page);
                    } finally {
                        latch.countDown();
                    }
                });

            }
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // When
        verify(bookQueueService, times(threadCnt)).addBookInfosToQueue(bookInfosCaptor.capture());
        List<BookInfo> capturedBookInfos = bookInfosCaptor.getAllValues().stream()
            .flatMap(List::stream)
            .toList();

        // Then
        assertThat(capturedBookInfos).hasSize(threadCnt);
        for (int i = 0; i < threadCnt; i++) {
            String expectedUrl = "http://test" + i + ".com";

            long count = capturedBookInfos.stream()
                .filter(bookInfo -> bookInfo.getBaseUrl().equals(expectedUrl))
                .count();

            assertThat(count).isEqualTo(1);
        }

    }


    private Page createMockPage(String url) {
        String htmlContent = """
                <ul class="book_resultList">
                    <li>
                        <div class="img">
                            <a href="/content/contentView.ink" title="테스트 책 제목 | ASML 전자도서관" class="scale" onclick="javascript:contentList.fnContentClick(this, '001', 'TEST1234567890', '110101', '', 'N', '5'); event.preventDefault();">
                                <img src="//example.com/book-cover.jpg" alt="테스트 책 제목" onerror="this.src='/resources/common/images/L_NOBOOK.jpg'"/>
                            </a>
                            <p><a href="#" class="btn btn_s btn_line" onClick="javascript:contentList.fnContentPreview('N','001', '110101' ,'TEST1234567890','5', ''); event.preventDefault();" target="_blank" title="새 창으로 열림">미리보기</a></p>
                        </div>
                        <div>
                            <p>
                                <span class="store">TestStore</span>
                            </p>
                            <ul>
                                <li class="tit"><a href="/content/contentView.ink" title="테스트 책 제목 | ASML 전자도서관" onClick="javascript:contentList.fnContentClick(this, '001', 'TEST1234567890', '110101', '', 'N'); event.preventDefault();">테스트 책 제목</a></li>
                                <li class="writer">제이든<span>테스트 출판사</span>2024-01-01</li>
                                <li class="txt">이것은 테스트 책의 설명입니다. 여기에 책의 줄거리나 소개가 들어갑니다. 이 책은 테스트를 위해 만들어진 가상의 책으로, 실제 존재하지 않습니다. 테스트 데이터의 구조와 형식을 보여주기 위한 목적으로 작성되었습니다.</li>
                            </ul>
                        </div>
                        <div class="btn_area">
                            <span><input type="button" name="brwBtn" value="대출" class="btn btn_mL btn_blue2" onClick="javascript:contentList.fnContentBorrow('N', '', '22705', '', 'TEST1234567890', '001', '테스트 책 제목','5', 'KB','1', '', event);"/></span>
                            <span><input type="button" name="dibsBtn" value="찜" class="btn btn_mL btn_sel" onClick="javascript:contentList.fnDibs(this, 'TEST1234567890','N');"/></span>
                        </div>
                    </li>
                </ul>
            """;

        Page page = mock(Page.class);
        when(page.getUrl()).thenReturn(new PlainText(url));
        when(page.getHtml()).thenReturn(Html.create(htmlContent));

        return page;

    }
}
