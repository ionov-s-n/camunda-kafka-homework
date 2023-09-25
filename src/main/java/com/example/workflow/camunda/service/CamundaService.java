package com.example.workflow.camunda.service;

import com.example.workflow.camunda.domain.RequestFields;
import com.example.workflow.dto.ValidationExceptionMessage;
import com.example.workflow.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CamundaService {

    @Value("${constants.main-process-key}")
    private String mainProcessKey;
    @Value("${constants.register-input-request-task-key}")
    private String registerInputRequestTaskKey;
    @Value("${constants.max-attempts}")
    private Integer maxAttempts;

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    public void correlateMessage(String msgRef, ValidationExceptionMessage message) {
        runtimeService.createMessageCorrelation(msgRef)
                .setVariables(Mapper.toExecutionContext(message))
                .correlate();
    }

    public void terminateProcess(String processInstanceId) {
        runtimeService.deleteProcessInstance(processInstanceId, "ValidationException occurred");
    }

    public void initContextAndRunMainProcess(String userName) {
        var processId = runtimeService.startProcessInstanceByKey(mainProcessKey).getId();
        var taskId = taskService
                .createTaskQuery()
                .active()
                .processInstanceId(processId)
                .taskDefinitionKey(registerInputRequestTaskKey)
                .singleResult()
                .getId();
        taskService.complete(taskId, Map.of(RequestFields.USER_NAME, userName,
                                            RequestFields.ATTEMPTS_LEFT, maxAttempts,
                                            RequestFields.USER_KEY, UUID.randomUUID().toString()));
    }
}