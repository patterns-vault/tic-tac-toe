package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.service.EndGameDeterminationService;
import com.scentbird.tictactoe.tictactoe.service.EndGameService;
import com.scentbird.tictactoe.tictactoe.service.GameMoveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameMoveKafkaConsumerTest {

    @InjectMocks
    private GameMoveKafkaConsumer gameMoveKafkaConsumer;

    @Mock
    private GameMoveService gameMoveService;

    @Mock
    private EndGameDeterminationService endGameDeterminationService;

    @Mock
    private EndGameService endGameService;

    private static final Integer MOVE_DELAY = 1000;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(gameMoveKafkaConsumer, "moveDelay", MOVE_DELAY);
    }

    @Test
    void testConsume_ShouldSaveGameMoveAndHandleEndGame_WhenGameIsFinished() {
        // Arrange
        GameMove mockGameMove = new GameMove();
        mockGameMove.setCurrentBoardState("XOXOXOXOX");
        EndGameStatus mockEndGameStatus = EndGameStatus.builder()
                .gameStatus(GameStatus.FINISHED)
                .build();


        when(endGameDeterminationService.shouldGameEnd(mockGameMove.getCurrentBoardState()))
                .thenReturn(mockEndGameStatus);

        // Act
        gameMoveKafkaConsumer.consume(mockGameMove);

        // Assert
        verify(gameMoveService, times(1)).saveGameMove(mockGameMove);
        verify(endGameDeterminationService, times(1))
                .shouldGameEnd(mockGameMove.getCurrentBoardState());
        verify(endGameService, times(1)).persistEndGame(mockGameMove, mockEndGameStatus);
    }

    @Test
    void testConsume_ShouldSaveGameMove_WhenGameIsNotFinished() {
        // Arrange
        GameMove mockGameMove = new GameMove();
        mockGameMove.setCurrentBoardState("XOXOXOXOX");
        EndGameStatus mockEndGameStatus = EndGameStatus.builder()
                .gameStatus(GameStatus.IN_PROGRESS)
                .build();

        when(endGameDeterminationService.shouldGameEnd(mockGameMove.getCurrentBoardState()))
                .thenReturn(mockEndGameStatus);

        // Act
        gameMoveKafkaConsumer.consume(mockGameMove);

        // Assert
        verify(gameMoveService, times(1)).saveGameMove(mockGameMove);
        verify(endGameDeterminationService, times(1))
                .shouldGameEnd(mockGameMove.getCurrentBoardState());
        verify(endGameService, never()).persistEndGame(any(), any());
    }

}
