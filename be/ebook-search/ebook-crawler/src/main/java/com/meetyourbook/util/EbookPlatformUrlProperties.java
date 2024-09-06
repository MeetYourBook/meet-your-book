package com.meetyourbook.util;

import com.meetyourbook.entity.Library.EbookPlatform;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "ebook")
@PropertySource(value = "file:/app/config/ebook-config.properties", ignoreResourceNotFound = true)
@Getter
@Setter
public class EbookPlatformUrlProperties {

    private final Map<EbookPlatform, String> platformUrls = new HashMap<>();

}