package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import com.scentbird.tictactoe.tictactoe.kafka.FirstTurnClaimKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirstTurnClaimService {

    private final FirstTurnClaimKafkaProducer firstTurnClaimKafkaProducer;

    @Value("${spring.application.name}")
    private String instanceName;

    public void initiateFirstTurnClaim() {
        FirstTurnClaimDto firstTurnClaimDto = buildFirstTurnClaimDto();
        firstTurnClaimKafkaProducer.sendMessage(firstTurnClaimDto);
    }

    private FirstTurnClaimDto buildFirstTurnClaimDto() {
        return FirstTurnClaimDto.builder()
                .uuid(UUID.randomUUID().toString())
                .instanceName(instanceName)
                .build();
    }
}
