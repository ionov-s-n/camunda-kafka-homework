package com.example.workflow.mapper;

import com.example.workflow.camunda.domain.RequestFields;
import com.example.workflow.dto.ValidationExceptionMessage;
import lombok.experimental.UtilityClass;
import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.Map;

@UtilityClass
public final class Mapper {
    public static Map<String, Object> toExecutionContext(ValidationExceptionMessage message) {
        return Map.of(RequestFields.USER_NAME, message.getPayload(),
                      RequestFields.ATTEMPTS_LEFT, message.getHeaders().get(RequestFields.ATTEMPTS_LEFT),
                      RequestFields.USER_KEY, message.getHeaders().get(RequestFields.USER_KEY));
    }

    public static ValidationExceptionMessage toValidationExceptionMessage(DelegateExecution execution) {
        return new ValidationExceptionMessage(
                (String) execution.getVariable(RequestFields.USER_NAME),
                Map.of(RequestFields.ATTEMPTS_LEFT, execution.getVariable(RequestFields.ATTEMPTS_LEFT),
                       RequestFields.USER_KEY, execution.getVariable(RequestFields.USER_KEY)));
    }
}
