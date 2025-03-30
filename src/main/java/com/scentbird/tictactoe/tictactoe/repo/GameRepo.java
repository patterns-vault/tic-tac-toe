package com.scentbird.tictactoe.tictactoe.repo;

import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GameRepo extends JpaRepository<Game, String> {

    Optional<Game> findTopByStatusOrderByCreatedAtDesc(GameStatus gameStatus);

    Optional<Game> findByGameId(UUID gameId);
}
