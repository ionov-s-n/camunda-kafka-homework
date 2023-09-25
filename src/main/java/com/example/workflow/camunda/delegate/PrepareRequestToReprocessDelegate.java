package com.example.workflow.camunda.delegate;

import com.example.workflow.camunda.domain.RequestFields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrepareRequestToReprocessDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        log.info("{}: Попью водички и в бой.", execution.getVariable(RequestFields.USER_KEY));
    }
}
