package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import com.scentbird.tictactoe.tictactoe.dto.EndGameType;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndGameDeterminationServiceTest {

    private final EndGameDeterminationService service = new EndGameDeterminationService();

    @Test
    void testShouldGameEnd_InProgressGame() {
        // Input: Game board with no winner and empty spaces
        String boardState = """
                X-O-X-O---
                """;

        EndGameStatus result = service.shouldGameEnd(boardState);

        assertNotNull(result);
        assertEquals(GameStatus.IN_PROGRESS, result.getGameStatus());
        assertNull(result.getWinner());
        assertNull(result.getEndGameType());
    }

    @Test
    void testShouldGameEnd_Draw() {
        // Input: Full board with no winner
        String boardState = """
                XOXXOXOXO
                """;

        EndGameStatus result = service.shouldGameEnd(boardState);

        assertNotNull(result);
        assertEquals(GameStatus.FINISHED, result.getGameStatus());
        assertEquals(EndGameType.DRAW, result.getEndGameType());
        assertNull(result.getWinner());
    }

    @Test
    void testShouldGameEnd_Victory() {
        // Input: Player X wins with top row
        String boardState = """
                XXXOO----
                """;

        EndGameStatus result = service.shouldGameEnd(boardState);

        assertNotNull(result);
        assertEquals(GameStatus.FINISHED, result.getGameStatus());
        assertEquals(EndGameType.VICTORY, result.getEndGameType());
        assertEquals(PlayerRole.X, result.getWinner());
    }

    @Test
    void testIsVictory_PlayerXWins() {
        // Input: Board where X has a winning condition
        String boardState = """
                X--X--X--
                """;

        boolean isVictory = service.isVictory(boardState, PlayerRole.X);

        assertTrue(isVictory);
    }

    @Test
    void testIsVictory_PlayerOWins() {
        // Input: Board where O has a winning condition (diagonal)
        String boardState = """
                O---O---O
                """;

        boolean isVictory = service.isVictory(boardState, PlayerRole.O);

        assertTrue(isVictory);
    }

    @Test
    void testIsVictory_NoVictory() {
        // Input: No player meets winning conditions
        String boardState = """
                XOXXOXOXO
                """;

        boolean isVictory = service.isVictory(boardState, PlayerRole.X);

        assertFalse(isVictory);
    }
}
