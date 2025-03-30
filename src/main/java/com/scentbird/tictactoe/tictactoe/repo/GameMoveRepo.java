package com.scentbird.tictactoe.tictactoe.repo;

import com.scentbird.tictactoe.tictactoe.model.GameMove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GameMoveRepo extends JpaRepository<GameMove, Long> {

    Optional<GameMove> findTopByGameIdOrderByCreatedAtDesc(UUID gameId);

    @Query("SELECT gm FROM GameMove gm " +
            "JOIN Game g ON g.gameId = gm.gameId " +
            "WHERE g.status = 'IN_PROGRESS' AND gm.moveNumber = " +
            "    (SELECT MAX(gmInner.moveNumber) FROM GameMove gmInner WHERE gmInner.gameId = g.gameId) " +
            "ORDER BY g.createdAt DESC")
    Optional<GameMove> findLatestGameMoveWithLargestMoveNumber();
}
