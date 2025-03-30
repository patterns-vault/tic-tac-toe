package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import com.scentbird.tictactoe.tictactoe.dto.EndGameType;
import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import com.scentbird.tictactoe.tictactoe.repo.GameRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndGameService {

    private final GameRepo gameRepo;

    private final static String DRAW = "draw";

    public void persistEndGame(GameMove victoriousMove, EndGameStatus endGameStatus) {
        UUID gameId = victoriousMove.getGameId();
        PlayerRole currentPlayerRole = victoriousMove.getCurrentPlayerRole();

        Game game = gameRepo.findByGameId(gameId)
                .orElseThrow(() -> new IllegalStateException("No game with gameId" + gameId));

        if (EndGameType.VICTORY == endGameStatus.getEndGameType()) {
            persistVictory(game, currentPlayerRole.name());
        }

        if (EndGameType.DRAW == endGameStatus.getEndGameType()) {
            persistDraw(game);
        }
    }

    private void persistVictory(Game game, String winner) {
        updateGame(game, winner);
        log.info(
                "The game with id: {}, finished. The winner is: {}",
                game.getGameId(),
                winner
        );
    }

    private void persistDraw(Game game) {
        updateGame(game, DRAW);
        log.info(
                "The game with id: {}, finished. It's a DRAW!",
                game.getGameId()
        );
    }

    private void updateGame(Game game,String winnerMessage) {
        game.setStatus(GameStatus.FINISHED);
        game.setWinner(winnerMessage);
        gameRepo.save(game);
    }
}
