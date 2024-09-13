package com.meetyourbook.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.meetyourbook.config.KyoboParserConfig;
import com.meetyourbook.dto.BookInfo;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KyoboParserTest {

    @Mock
    private KyoboParserConfig kyoboParserConfig;

    private KyoboParser kyoboParser;
    private String htmlContent;
    private String url;
    private Document doc;

    @BeforeEach
    void setUp() {
        htmlContent = """
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
        url = "https://example.com/content/contentList.ink?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=20&pageIndex=1&recordCount=20";
        doc = Jsoup.parse(htmlContent, url);

        kyoboParser = new KyoboParser(kyoboParserConfig);

        when(kyoboParserConfig.getBookSelector()).thenReturn("book_resultList");
        when(kyoboParserConfig.getTitleSelector()).thenReturn(".tit");
        when(kyoboParserConfig.getAuthorSelector()).thenReturn(".writer");
        when(kyoboParserConfig.getPublisherSelector()).thenReturn(".writer span");
        when(kyoboParserConfig.getPublishDateSelector()).thenReturn(".writer");
        when(kyoboParserConfig.getImageSelector()).thenReturn("img");
    }


    @Test
    @DisplayName("저자 파싱 테스트")
    void testParsingAuthor() {
        // Given

        // When
        List<BookInfo> bookInfos = kyoboParser.parseBooks(doc);
        BookInfo bookInfo = bookInfos.getFirst();

        // Then

        assertThat(bookInfos).hasSize(1);
        assertThat(bookInfo.author()).isEqualTo("제이든");

    }

    @Test
    @DisplayName("책 제목 파싱 테스트")
    void testParsingTitle() {
        // Given

        // When
        List<BookInfo> bookInfos = kyoboParser.parseBooks(doc);
        BookInfo bookInfo = bookInfos.getFirst();

        // Then
        assertThat(bookInfo.title()).isEqualTo("테스트 책 제목");

    }

    @Test
    @DisplayName("출판사 파싱 테스트")
    void testParsingPublisher() {
        // Given

        // When
        List<BookInfo> bookInfos = kyoboParser.parseBooks(doc);
        BookInfo bookInfo = bookInfos.getFirst();

        // Then
        assertThat(bookInfo.publisher()).isEqualTo("테스트 출판사");

    }

    @Test
    @DisplayName("출판일 파싱 테스트")
    void testParsingPublishDate() {
        // Given

        // When
        List<BookInfo> bookInfos = kyoboParser.parseBooks(doc);
        BookInfo bookInfo = bookInfos.getFirst();

        // Then
        assertThat(bookInfo.publishDate()).isEqualTo("2024-01-01");

    }

    @Test
    @DisplayName("이미지 URL 파싱 테스트")
    void testParsingImageUrl() {
        // Given

        // When
        List<BookInfo> bookInfos = kyoboParser.parseBooks(doc);
        BookInfo bookInfo = bookInfos.getFirst();

        // Then
        assertThat(bookInfo.imageUrl()).isEqualTo("example.com/book-cover.jpg");

    }

    @Test
    @DisplayName("책 URL 파싱 테스트")
    void testParsingBookUrl() {
        // Given

        // When
        List<BookInfo> bookInfos = kyoboParser.parseBooks(doc);
        BookInfo bookInfo = bookInfos.getFirst();

        // Then
        assertThat(bookInfo.bookUrl()).isEqualTo(
            "https://example.com/content/contentView.ink?cttsDvsnCode=001&brcd=TEST1234567890&sntnAuthCode=&ctgrId=110101");

    }

}
