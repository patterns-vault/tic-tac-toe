package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import com.scentbird.tictactoe.tictactoe.dto.EndGameType;
import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import com.scentbird.tictactoe.tictactoe.repo.GameRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EndGameServiceTest {

    @Mock
    private GameRepo gameRepo;

    @InjectMocks
    private EndGameService endGameService;

    private Game game;
    private UUID gameId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        gameId = UUID.randomUUID();
        game = new Game();
        game.setGameId(gameId);
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setWinner(null);
    }

    @Test
    void testPersistEndGame_Victory() {
        // Arrange
        GameMove winningMove = new GameMove();
        winningMove.setGameId(gameId);
        winningMove.setCurrentPlayerRole(PlayerRole.X);

        EndGameStatus endGameStatus = new EndGameStatus();
        endGameStatus.setEndGameType(EndGameType.VICTORY);

        when(gameRepo.findByGameId(gameId)).thenReturn(Optional.of(game));

        // Act
        endGameService.persistEndGame(winningMove, endGameStatus);

        // Assert
        verify(gameRepo).save(game);
        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertEquals("X", game.getWinner());
    }

    @Test
    void testPersistEndGame_Draw() {
        // Arrange
        GameMove drawMove = new GameMove();
        drawMove.setGameId(gameId);
        drawMove.setCurrentPlayerRole(PlayerRole.X);

        EndGameStatus endGameStatus = new EndGameStatus();
        endGameStatus.setEndGameType(EndGameType.DRAW);

        when(gameRepo.findByGameId(gameId)).thenReturn(Optional.of(game));

        // Act
        endGameService.persistEndGame(drawMove, endGameStatus);

        // Assert
        verify(gameRepo).save(game);
        assertEquals(GameStatus.FINISHED, game.getStatus());
        assertEquals("draw", game.getWinner());
    }

    @Test
    void testPersistEndGame_ThrowsException() {
        // Arrange
        GameMove winningMove = new GameMove();
        winningMove.setGameId(gameId);
        winningMove.setCurrentPlayerRole(PlayerRole.X);

        EndGameStatus endGameStatus = new EndGameStatus();
        endGameStatus.setEndGameType(EndGameType.VICTORY);

        when(gameRepo.findByGameId(gameId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                endGameService.persistEndGame(winningMove, endGameStatus)
        );

        assertEquals("No game with gameId" + gameId, exception.getMessage());
    }
}
