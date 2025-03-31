package com.scentbird.tictactoe.tictactoe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledJob {

    @Value("${spring.application.name}")
    private String instanceName;

    private final NextActionService nextActionService;

    @Scheduled(fixedRateString = "${tic-tac-toe.move.delay}") // Runs every 5 seconds
    public void fetchDataFromDatabase() {
        log.info("Scheduled Job is being executed!");
        nextActionService.executeNextAction();
    }

}
