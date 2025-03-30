package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.model.GameMove;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameMoveKafkaProducer {

    private final KafkaTemplate<String, GameMove> kafkaTemplate;

    @Value("${tic-tac-toe.kafka.game-move.topic.name}")
    private String gameMoveTopicName;

    public void sendMessage(GameMove gameMove) {
        log.info("Sending GameMove: {}", gameMove);
        kafkaTemplate.send(gameMoveTopicName, gameMove);
    }

}
