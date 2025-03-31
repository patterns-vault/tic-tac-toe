package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.NextActionDataContext;
import com.scentbird.tictactoe.tictactoe.kafka.GameMoveKafkaProducer;
import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NextActionService {

    private final GameService gameService;
    private final FirstTurnClaimService firstTurnClaimService;
    private final GameMoveService gameMoveService;
    private final GameMoveKafkaProducer gameMoveKafkaProducer;
    private final InstanceRoleService instanceRoleService;

    @Value("${spring.application.name}")
    private String instanceName;

    public void executeNextAction() {
        NextActionDataContext context = new NextActionDataContext();
        if (areNoActiveGamesYet(context)) {
            log.info("There are no games in progress");
            initiateFirstTurnClaim();
            return;
        }

        if (areNoMovesInGame(context)) {
            UUID gameId = context.getCurrentGameOptional().get().getGameId();
            log.info("Game {} has no moves yet!", gameId);
            if (isThisInstanceFirstTurn(context)) {
                executeFirstMove(gameId);
            }
            return;
        }

        if (isThisInstanceMoveTurn(context)) {
            executeNextMove(context);
        }
    }

    private boolean areNoActiveGamesYet(NextActionDataContext context) {
        Optional<Game> unfinishedGameOptional = gameService.getMostRecentUnfinishedGame();
        context.setCurrentGameOptional(unfinishedGameOptional);
        return unfinishedGameOptional.isEmpty();
    }

    private boolean areNoMovesInGame(NextActionDataContext context) {
        Game currentGame = context.getCurrentGameOptional()
                .get();
        UUID gameId = currentGame
                .getGameId();
        Optional<GameMove> lastGameMoveOptional = getLastGameMove(gameId);
        context.setLastGameMoveOptional(lastGameMoveOptional);
        return lastGameMoveOptional.isEmpty();
    }

    private boolean isThisInstanceFirstTurn(NextActionDataContext context) {
        String firstTurn = context.getCurrentGameOptional().get().getFirstTurn();
        return instanceName.equals(firstTurn);
    }

    private void initiateFirstTurnClaim() {
        firstTurnClaimService.initiateFirstTurnClaim();
    }

    private boolean isThisInstanceMoveTurn(NextActionDataContext context) {
        PlayerRole nextMoveRole = context.getLastGameMoveOptional().get().getNextMoveRole();
        PlayerRole currentRole = instanceRoleService.getCurrentRole();
        context.setCurrentRole(currentRole);
        return nextMoveRole == currentRole;
    }

    private Optional<GameMove> getLastGameMove(UUID gameId) {
        return gameMoveService.getLastGameMove(gameId);
    }

    private void executeFirstMove(UUID gameId) {
        String prevBoardState = "---------";
        String currentBoardState = insertRandomlySymbol(prevBoardState, PlayerRole.X);

        GameMove gameMove = GameMove.builder()
                .gameId(gameId)
                .prevBoardState(prevBoardState)
                .currentBoardState(currentBoardState)
                .moveNumber(1)
                .currentPlayerRole(PlayerRole.X)
                .nextMoveRole(PlayerRole.X.other())
                .currentInstance(instanceName)
                .build();

        gameMoveKafkaProducer.sendMessage(gameMove);
    }

    private void executeNextMove(NextActionDataContext context) {
        GameMove lastGameMove = context.getLastGameMoveOptional().get();
        PlayerRole currentRole = context.getCurrentRole();
        String prevBoardState = lastGameMove.getCurrentBoardState();
        String currentBoardState = insertRandomlySymbol(prevBoardState, currentRole);

        GameMove nextGameMove = GameMove.builder()
                .gameId(lastGameMove.getGameId())
                .prevBoardState(lastGameMove.getCurrentBoardState())
                .currentBoardState(currentBoardState)
                .moveNumber(lastGameMove.getMoveNumber() + 1)
                .currentPlayerRole(currentRole)
                .nextMoveRole(currentRole.other())
                .currentInstance(instanceName)
                .build();

        gameMoveKafkaProducer.sendMessage(nextGameMove);
    }

    private String insertRandomlySymbol(String boardState, PlayerRole playerRole) {
        List<Integer> emptyBoardPositions = getEmptyBoardPositions(boardState);

        if (emptyBoardPositions.isEmpty()) {
            return boardState;
        }

        // Choose a random dash position
        Random random = new Random();
        int randomIndex = random.nextInt(emptyBoardPositions.size());
        int positionToReplace = emptyBoardPositions.get(randomIndex);

        return boardState.substring(0, positionToReplace) + playerRole.name() + boardState.substring(positionToReplace + 1);
    }

    private List<Integer> getEmptyBoardPositions(String boardState) {
        List<Integer> dashPositions = new ArrayList<>();

        for (int i = 0; i < boardState.length(); i++) {
            if (boardState.charAt(i) == '-') {
                dashPositions.add(i);
            }
        }
        return dashPositions;
    }
}
