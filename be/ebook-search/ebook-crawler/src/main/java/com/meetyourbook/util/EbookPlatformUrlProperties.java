package com.meetyourbook.util;

import com.meetyourbook.entity.Library.EbookPlatform;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ebook")
@PropertySource(value = "classpath:ebook-config.properties", encoding = "UTF-8")
@Getter
@Setter
public class EbookPlatformUrlProperties {

    private final Map<EbookPlatform, String> platformUrls = new HashMap<>();


}
