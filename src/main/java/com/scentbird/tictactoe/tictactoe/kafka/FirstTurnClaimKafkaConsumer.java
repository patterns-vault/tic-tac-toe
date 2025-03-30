package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import com.scentbird.tictactoe.tictactoe.service.FirstTurnResolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirstTurnClaimKafkaConsumer {

    private final FirstTurnResolutionService firstTurnResolutionService;

    @KafkaListener(
            topics = "${tic-tac-toe.kafka.first-turn-claim.topic.name}",
            groupId = "${spring.kafka.consumer.first-turn-claim.group-id}",
            containerFactory = "firstTurnKafkaListenerContainerFactory"
    )
    public void consume(FirstTurnClaimDto firstTurnClaimDto) {
        log.info("Consumed FirstTurnClaimDto: {}", firstTurnClaimDto);
        firstTurnResolutionService.submitClaim(firstTurnClaimDto);
    }
}
