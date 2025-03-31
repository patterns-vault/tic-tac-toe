package com.scentbird.tictactoe.tictactoe.dto;

import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import lombok.*;

import java.util.Optional;

// The class exists only for readability purposes to be a data holder
// for a method NextActionService#executeNextAction. All the Optional fields
// are guaranteed a safe get() invocation in the method's execution flow
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
