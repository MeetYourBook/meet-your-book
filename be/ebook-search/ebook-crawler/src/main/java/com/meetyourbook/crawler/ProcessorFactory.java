package com.meetyourbook.crawler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.processor.PageProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessorFactory {

    private final ApplicationContext applicationContext;

    public PageProcessor getProcessor(ProcessorType processorType) {
        String processorName = processorType.getBeanName();
        if (applicationContext.containsBean(processorName)) {
            return (PageProcessor) applicationContext.getBean(processorName);
        }
        throw new IllegalArgumentException(
            "알 수 없는 크롤링 프로세서 이름입니다. bean으로 등록 했는지 확인해주세요: " + processorName);
    }
}
