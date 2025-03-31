package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.repo.GameRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameServiceTest {

    @Mock
    private GameRepo gameRepo;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize the mocks
    }

    @Test
    void testGetMostRecentUnfinishedGame_WhenUnfinishedGameExists() {
        // Arrange
        Game unfinishedGame = new Game();
        unfinishedGame.setStatus(GameStatus.IN_PROGRESS);
        Optional<Game> expectedGame = Optional.of(unfinishedGame);
        when(gameRepo.findTopByStatusOrderByCreatedAtDesc(GameStatus.IN_PROGRESS)).thenReturn(expectedGame);

        // Act
        Optional<Game> actualGame = gameService.getMostRecentUnfinishedGame();

        // Assert
        assertEquals(expectedGame, actualGame);
        verify(gameRepo, times(1)).findTopByStatusOrderByCreatedAtDesc(GameStatus.IN_PROGRESS);
    }

    @Test
    void testGetMostRecentUnfinishedGame_WhenNoUnfinishedGameExists() {
        // Arrange
        Optional<Game> expectedGame = Optional.empty();
        when(gameRepo.findTopByStatusOrderByCreatedAtDesc(GameStatus.IN_PROGRESS)).thenReturn(expectedGame);

        // Act
        Optional<Game> actualGame = gameService.getMostRecentUnfinishedGame();

        // Assert
        assertEquals(expectedGame, actualGame);
        verify(gameRepo, times(1)).findTopByStatusOrderByCreatedAtDesc(GameStatus.IN_PROGRESS);
    }
}
