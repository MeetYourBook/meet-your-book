package com.meetyourbook.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.meetyourbook.entity.Library.LibraryType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LibraryTest {

    @Test
    @DisplayName("파싱을 위한 최종 url 생성에 문제가 없는지 테스트")
    void testUrlCreation() {
        Library library = Library.builder()
            .name("example")
            .type(LibraryType.UNIVERSITY_LIBRARY)
            .libraryUrl("https://ebook.howon.ac.kr:444/elibrary-front/main.ink")
            .totalBookCount(0)
            .build();

        String url = library.getUrlWithQueryParameters(1);
        assertThat(url).isEqualTo("https://ebook.howon.ac.kr:444/elibrary-front/content/contentList.ink?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=1&pageIndex=1&recordCount=20");
    }


}
