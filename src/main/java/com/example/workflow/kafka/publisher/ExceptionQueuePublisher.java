package com.example.workflow.kafka.publisher;

import com.example.workflow.dto.ValidationExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExceptionQueuePublisher {

    private final KafkaTemplate<String, ValidationExceptionMessage> kafkaTemplate;

    @Value("${kafka.topics.exception-queue}")
    private String exceptionQueueTopic;

    public void publish(ValidationExceptionMessage message) {
        kafkaTemplate.send(exceptionQueueTopic, message);
    }
}
