package com.meetyourbook.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.meetyourbook.entity.Library.EbookPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@EnableConfigurationProperties(EbookPlatformUrlProperties.class)
@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:ebook-config.properties")
class EbookPlatformUrlPropertiesTest {

    @Autowired
    private EbookPlatformUrlProperties ebookPlatformUrlProperties;

    @Test
    @DisplayName("프로퍼티 파일이 정상적으로 로드되어서 EbookPlatformUrlProperties 빈이 생성되었는지 확인")
    void testEbookPlatformUrlPropertiesLoaded() {
        assertThat(ebookPlatformUrlProperties).isNotNull();
    }

    @Test
    @DisplayName("프로퍼티 파일에서 KYOBO 플랫폼의 URL을 정확히 읽어오는지 확인")
    void testGetKyoboUrl() {
        assertThat(ebookPlatformUrlProperties.getPlatformUrls().get(EbookPlatform.KYOBO))
            .isEqualTo("https://kyobo.com/test");
    }

    @Test
    @DisplayName("모든 EbookPlatform enum 값에 대해 URL이 설정되어 있는지 확인")
    void testAllPlatformsHaveUrl() {
        for (EbookPlatform platform : EbookPlatform.values()) {
            assertThat(ebookPlatformUrlProperties.getPlatformUrls().get(platform))
                .as("%s 플랫폼의 URL이 설정되어 있어야 합니다", platform)
                .isNotNull()
                .isNotEmpty();
        }
    }

    @Test
    @DisplayName("플랫폼 URL 맵에 접근 시 NPE가 발생하지 않는지 확인")
    void testNullSafetyOfPlatformUrls() {
        assertThatCode(() -> ebookPlatformUrlProperties.getPlatformUrls()
            .get(EbookPlatform.KYOBO)).doesNotThrowAnyException();
    }
}