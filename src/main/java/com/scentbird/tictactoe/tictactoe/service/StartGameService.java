package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.repo.GameRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartGameService {

    private final GameRepo gameRepo;

    public void startGame(String uuid, String firstTurn) {
        Game game = Game.builder()
                .gameId(UUID.fromString(uuid))
                .firstTurn(firstTurn)
                .status(GameStatus.IN_PROGRESS)
                .build();
        gameRepo.save(game);
    }
}
