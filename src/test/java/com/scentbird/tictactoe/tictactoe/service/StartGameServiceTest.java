package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.repo.GameRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StartGameServiceTest {

    @Mock
    private GameRepo gameRepo;

    @InjectMocks
    private StartGameService startGameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartGame_CreatesAndSavesGame() {
        // Given
        String uuid = "123e4567-e89b-12d3-a456-556642440000";
        String firstTurn = "X";

        // When
        startGameService.startGame(uuid, firstTurn);

        // Then
        verify(gameRepo, times(1)).save(argThat(game ->
                game.getGameId().equals(UUID.fromString(uuid)) &&
                        game.getFirstTurn().equals(firstTurn) &&
                        game.getStatus() == GameStatus.IN_PROGRESS
        ));
    }
}
