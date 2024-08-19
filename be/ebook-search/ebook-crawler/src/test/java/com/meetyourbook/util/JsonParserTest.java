package com.meetyourbook.util;

import static org.assertj.core.groups.Tuple.tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetyourbook.common.exception.JsonParseException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class JsonParserTest {

    private JsonParser jsonConvertor;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        jsonConvertor = new JsonParser(objectMapper);
    }

    @Test
    @DisplayName("JSON 파일과 클래스 타입을 입력받아 파싱한다.")
    void parseJsonFile() {
        // Given
        String jsonContent = "[{\"name\":\"Jayden\",\"age\":27},{\"name\":\"George\",\"age\":29}]";
        MockMultipartFile file = new MockMultipartFile(
            "file", "test.json", "application/json", jsonContent.getBytes()
        );

        // When
        List<TestData> result = jsonConvertor.parseJsonFile(file, TestData.class);

        // Then
        Assertions.assertThat(result).hasSize(2)
            .extracting(
                TestData::getName,
                TestData::getAge
            )
            .containsExactlyInAnyOrder(
                tuple("Jayden", 27),
                tuple("George", 29)
            );
    }

    @Test
    @DisplayName("JSON 파일 형식이 올바르지 않을 경우 JsonParseException를 발생시킨다.")
    void parseInvalidJsonFile() {
        // Given
        String invalidJsonContent = "This is not a valid JSON";
        MockMultipartFile file = new MockMultipartFile(
            "file", "invalid.json", "application/json", invalidJsonContent.getBytes()
        );

        // When & Then
        Assertions.assertThatThrownBy(() -> jsonConvertor.parseJsonFile(file, TestData.class))
            .isInstanceOf(JsonParseException.class);
    }

    private static class TestData {

        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}