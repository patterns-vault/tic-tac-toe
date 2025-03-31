package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import lombok.*;

import java.util.Optional;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextActionDataContext {
    private Optional<Game> currentGameOptional;
    private Optional<GameMove> lastGameMoveOptional;
    private PlayerRole currentRole;
}
