package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GameMoveKafkaProducerTest {

    @InjectMocks
    private GameMoveKafkaProducer gameMoveKafkaProducer;

    @Mock
    private KafkaTemplate<String, GameMove> kafkaTemplate;


    private String TOPIC_NAME = "game-move-topic";

    @BeforeEach
    void setUp() {
        gameMoveKafkaProducer = new GameMoveKafkaProducer(kafkaTemplate);
        ReflectionTestUtils.setField(gameMoveKafkaProducer, "gameMoveTopicName", TOPIC_NAME);
    }

    @Test
    void testSendMessage() {
        // Arrange
        GameMove gameMove = GameMove.builder()
                .currentPlayerRole(PlayerRole.X)
                .moveNumber(3)
                .build();

        // Act
        gameMoveKafkaProducer.sendMessage(gameMove);

        // Assert
        verify(kafkaTemplate).send(TOPIC_NAME, gameMove);
    }
}
