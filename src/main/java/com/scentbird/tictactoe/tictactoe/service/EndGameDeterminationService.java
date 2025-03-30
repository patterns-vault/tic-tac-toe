package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import com.scentbird.tictactoe.tictactoe.dto.EndGameType;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import org.springframework.stereotype.Service;

@Service
public class EndGameDeterminationService {

    private final int[][] winConditions = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    public EndGameStatus shouldGameEnd(String boardState) {

        PlayerRole winner = null;

        for (PlayerRole role : PlayerRole.values()) {
            if (isVictory(boardState, role)) {
                winner = role;
                break;
            }
        }

        boolean isBoardStateFull = isBoardStateFull(boardState);

        // if no winner and board is not full -> continue the game
        if (!isBoardStateFull && winner == null) {
            return EndGameStatus.builder()
                    .gameStatus(GameStatus.IN_PROGRESS)
                    .build();
        }

        // if no winner and board IS full -> return draw
        if (isBoardStateFull && winner == null) {
            return EndGameStatus.builder()
                    .gameStatus(GameStatus.FINISHED)
                    .endGameType(EndGameType.DRAW)
                    .build();
        }

        // if there is a winner -> return victory
        if (winner != null) {
            return EndGameStatus.builder()
                    .gameStatus(GameStatus.FINISHED)
                    .endGameType(EndGameType.VICTORY)
                    .winner(winner)
                    .build();
        }

        String errorMessage = String.format(
                "Illegal Game Status. Board State: %s, isBoardStateFull: %s, winner: %s {}",
                boardState,
                isBoardStateFull,
                winner
        );
        throw new IllegalStateException(errorMessage);
    }

    public boolean isVictory(String boardState, PlayerRole playerRole) {
        char player = playerRole.name().charAt(0);

        for (int[] condition : winConditions) {
            if (boardState.charAt(condition[0]) == player &&
                    boardState.charAt(condition[1]) == player &&
                    boardState.charAt(condition[2]) == player) {
                return true;
            }
        }

        return false;
    }

    private boolean isBoardStateFull(String boardState) {
        return !boardState.contains("-");
    }
}
