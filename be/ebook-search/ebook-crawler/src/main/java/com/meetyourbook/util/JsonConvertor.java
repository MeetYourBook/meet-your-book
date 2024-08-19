package com.meetyourbook.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.meetyourbook.common.exception.JsonParseException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonConvertor {

    private final ObjectMapper objectMapper;

    public <T> List<T> parseJsonFile(MultipartFile file, Class<T> targetClass) {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, targetClass);
            return objectMapper.readValue(file.getInputStream(), listType);
        } catch (IOException e) {
            throw new JsonParseException();
        }
    }

}
