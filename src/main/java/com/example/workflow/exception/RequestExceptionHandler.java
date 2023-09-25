package com.example.workflow.exception;

import com.example.workflow.camunda.domain.RequestFields;
import com.example.workflow.camunda.service.CamundaService;
import com.example.workflow.kafka.publisher.DeadLetterQueuePublisher;
import com.example.workflow.kafka.publisher.ExceptionQueuePublisher;
import com.example.workflow.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestExceptionHandler {

    private final CamundaService camundaService;
    private final ExceptionQueuePublisher exceptionQueuePublisher;
    private final DeadLetterQueuePublisher deadLetterQueuePublisher;

    public void handleValidationException(DelegateExecution execution) {
        var message = Mapper.toValidationExceptionMessage(execution);
        camundaService.terminateProcess(execution.getProcessInstanceId());

        var attemptsLeft = (Integer) message.getHeaders().get(RequestFields.ATTEMPTS_LEFT);
        var userKey = (String) message.getHeaders().get(RequestFields.USER_KEY);

        if (attemptsLeft > 0) {
            log.info("{}: Подойду попозже..", userKey);
            exceptionQueuePublisher.publish(message);
        } else {
            log.info("{}: Терпения не осталось..", userKey);
            deadLetterQueuePublisher.publish(message);
        }
    }
}
