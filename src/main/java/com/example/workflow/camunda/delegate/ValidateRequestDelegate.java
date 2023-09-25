package com.example.workflow.camunda.delegate;

import com.example.workflow.camunda.domain.RequestFields;
import com.example.workflow.exception.RequestExceptionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateRequestDelegate implements JavaDelegate {

    private final RequestExceptionHandler requestExceptionHandler;

    @Override
    public void execute(DelegateExecution execution) {
        var userKey = (String) execution.getVariable(RequestFields.USER_KEY);
        var attemptsLeft = (Integer) execution.getVariable(RequestFields.ATTEMPTS_LEFT);
        execution.setVariable(RequestFields.ATTEMPTS_LEFT, --attemptsLeft);

        log.info("{}: Пришёл подавать заявление", userKey);

        if (roll()) {
            log.info("{}: Тёть Вали нет на месте.", userKey);
            requestExceptionHandler.handleValidationException(execution);
            return;
        }
        
        var isValid = roll();
        execution.setVariable(RequestFields.IS_VALID, isValid);
        log.info("{}: Спросил тёть Валю, можно ли пойти в отпуск", userKey);
    }

    private static boolean roll() {
        return Math.random() > 0.3;
    }

}
