package com.scentbird.tictactoe.tictactoe.dto;

import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndGameStatus {
    private EndGameType endGameType;
    private PlayerRole winner;
    private GameStatus gameStatus;
}
