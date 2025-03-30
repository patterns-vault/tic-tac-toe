package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.repo.GameMoveRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameMoveService {

    private final GameMoveRepo gameMoveRepo;

    public Optional<GameMove> getLastGameMove(UUID gameId) {
        return gameMoveRepo.findTopByGameIdOrderByCreatedAtDesc(gameId);
    }

    public void saveGameMove(GameMove gameMove) {
        gameMoveRepo.save(gameMove);
    }

    public String getLatestBoardState() {
        return gameMoveRepo.findLatestGameMoveWithLargestMoveNumber()
                .map(GameMove::getCurrentBoardState)
                .orElseGet(() -> "---------");
    }
}
