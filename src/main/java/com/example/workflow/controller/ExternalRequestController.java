package com.example.workflow.controller;

import com.example.workflow.camunda.service.CamundaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ExternalRequestController {

    private final CamundaService camundaService;

    @GetMapping("/{userName}")
    public void initRequest(@PathVariable("userName") String userName) {
        log.info("Пора идти в отпуск, {}", userName);
        camundaService.initContextAndRunMainProcess(userName);
    }
}
