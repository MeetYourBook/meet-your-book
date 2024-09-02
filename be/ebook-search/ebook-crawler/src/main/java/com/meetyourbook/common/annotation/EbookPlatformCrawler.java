package com.meetyourbook.common.annotation;

import com.meetyourbook.entity.Library.EbookPlatform;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface EbookPlatformCrawler {

    EbookPlatform value();

}
