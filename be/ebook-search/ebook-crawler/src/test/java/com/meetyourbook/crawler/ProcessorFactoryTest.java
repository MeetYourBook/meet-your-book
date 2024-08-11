package com.meetyourbook.crawler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.processor.PageProcessor;

@ExtendWith(MockitoExtension.class)
public class ProcessorFactoryTest {

    @Mock
    private ProcessorType processorType;
    @Mock
    private ApplicationContext applicationContext;

    private ProcessorFactory processorFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        processorFactory = new ProcessorFactory(applicationContext);
    }

    @Test
    @DisplayName("프로세서가 bean으로 등록되어 있으면 PageProcessor를 반환합니다.")
    void getRegisteredProcessor_ReturnPageProcessor() {
        // Given
        String beanName = "RegisteredProcessor";
        when(processorType.getBeanName()).thenReturn(beanName);
        when(applicationContext.containsBean(beanName)).thenReturn(true);
        PageProcessor expectedProcessor = (PageProcessor) applicationContext.getBean(beanName);

        // When
        PageProcessor pageProcessor = processorFactory.getProcessor(processorType);

        // Then
        assertThat(pageProcessor).isEqualTo(expectedProcessor);

    }

    @Test
    @DisplayName("프로세서를 bean으로 등록하지 않으면 argument 예외를 던집니다.")
    void getNotRegisteredProcessor_ShouldThrowException() {
        // Given
        String beanName = "TestPage";
        when(processorType.getBeanName()).thenReturn(beanName);
        when(applicationContext.containsBean(beanName)).thenReturn(false);

        // When, Then
        assertThatThrownBy(() -> processorFactory.getProcessor(processorType))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
