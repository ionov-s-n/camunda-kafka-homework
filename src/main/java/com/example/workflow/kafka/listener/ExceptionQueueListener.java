package com.example.workflow.kafka.listener;

import com.example.workflow.dto.ValidationExceptionMessage;
import com.example.workflow.camunda.service.CamundaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.topics.exception-queue}")
public class ExceptionQueueListener {

    @Value("${constants.exception-was-received-msg-ref}")
    private String exceptionWasReceivedMsgRef;

    private final CamundaService camundaService;

    @KafkaHandler
    public void consume(ValidationExceptionMessage message) {
        camundaService.correlateMessage(exceptionWasReceivedMsgRef, message);
    }
}
