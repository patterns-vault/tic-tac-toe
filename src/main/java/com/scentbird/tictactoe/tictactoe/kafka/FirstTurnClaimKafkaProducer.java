package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirstTurnClaimKafkaProducer {

    private final KafkaTemplate<String, FirstTurnClaimDto> kafkaTemplate;

    @Value("${tic-tac-toe.kafka.first-turn-claim.topic.name}")
    private String firstTurnClaimTopicName;

    public void sendMessage(FirstTurnClaimDto firstTurnClaimDto) {
        log.info("Sending first turn claim {}", firstTurnClaimDto);
        kafkaTemplate.send(firstTurnClaimTopicName, firstTurnClaimDto);
    }
}
