package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.repo.GameMoveRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameMoveServiceTest {

    @Mock
    private GameMoveRepo gameMoveRepo;

    @InjectMocks
    private GameMoveService gameMoveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLastGameMove_WhenExist() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        GameMove expectedGameMove = new GameMove(); // Replace with concrete values
        when(gameMoveRepo.findTopByGameIdOrderByCreatedAtDesc(gameId))
                .thenReturn(Optional.of(expectedGameMove));

        // Act
        Optional<GameMove> result = gameMoveService.getLastGameMove(gameId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedGameMove);
        verify(gameMoveRepo).findTopByGameIdOrderByCreatedAtDesc(gameId);
    }

    @Test
    void testGetLastGameMove_WhenNotExist() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        when(gameMoveRepo.findTopByGameIdOrderByCreatedAtDesc(gameId))
                .thenReturn(Optional.empty());

        // Act
        Optional<GameMove> result = gameMoveService.getLastGameMove(gameId);

        // Assert
        assertThat(result).isEmpty();
        verify(gameMoveRepo).findTopByGameIdOrderByCreatedAtDesc(gameId);
    }

    @Test
    void testSaveGameMove() {
        // Arrange
        GameMove gameMove = new GameMove(); // Replace with concrete values

        // Act
        gameMoveService.saveGameMove(gameMove);

        // Assert
        verify(gameMoveRepo).save(gameMove);
    }

    @Test
    void testGetLatestBoardState_WhenExists() {
        // Arrange
        GameMove gameMove = new GameMove();
        gameMove.setCurrentBoardState("XOXOXOXOX"); // Replace with concrete values
        when(gameMoveRepo.findLatestGameMoveWithLargestMoveNumber())
                .thenReturn(Optional.of(gameMove));

        // Act
        String result = gameMoveService.getLatestBoardState();

        // Assert
        assertThat(result).isEqualTo("XOXOXOXOX");
        verify(gameMoveRepo).findLatestGameMoveWithLargestMoveNumber();
    }

    @Test
    void testGetLatestBoardState_WhenNotExists() {
        // Arrange
        when(gameMoveRepo.findLatestGameMoveWithLargestMoveNumber())
                .thenReturn(Optional.empty());

        // Act
        String result = gameMoveService.getLatestBoardState();

        // Assert
        assertThat(result).isEqualTo("---------");
        verify(gameMoveRepo).findLatestGameMoveWithLargestMoveNumber();
    }
}
