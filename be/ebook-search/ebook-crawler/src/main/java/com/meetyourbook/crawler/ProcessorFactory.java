package com.meetyourbook.crawler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.processor.PageProcessor;

@Component
public class ProcessorFactory {

    private final Map<String, PageProcessor> processors;

    public ProcessorFactory(List<PageProcessor> processorList) {
        processors = processorList.stream()
            .collect(Collectors.toMap(
                processor -> processor.getClass().getSimpleName(),
                Function.identity()
            ));
    }

    public PageProcessor getProcessor(String processorName) {
        PageProcessor pageProcessor = processors.get(processorName);
        if (pageProcessor == null) {
            throw new IllegalArgumentException("잘못된 크롤링 프로세서 이름입니다: " + processorName);
        }
        return pageProcessor;
    }

}
