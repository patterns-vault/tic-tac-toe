package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.kafka.GameMoveKafkaProducer;
import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ScheduledJob {

    @Value("${spring.application.name}")
    private String instanceName;

    private final GameService gameService;
    private final GameMoveService gameMoveService;
    private final GameMoveKafkaProducer gameMoveKafkaProducer;
    private final InstanceRoleService instanceRoleService;
    private final FirstTurnClaimService firstTurnClaimService;

    @Scheduled(fixedRateString = "${tic-tac-toe.delay}") // Runs every 5 seconds
    public void fetchDataFromDatabase() {
        log.info("Scheduled Job is being executed!");
        run();
    }

    private void run() {
        Optional<Game> unfinishedGameOptional = gameService.getMostRecentUnfinishedGame();

        if (unfinishedGameOptional.isEmpty()) {
            log.info("There are no games in progress");
            firstTurnClaimService.initiateFirstTurnClaim();
            return;
        }

        Game currentGame = unfinishedGameOptional.get();
        UUID gameId = currentGame.getGameId();

        Optional<GameMove> lastGameMoveOptional = gameMoveService.getLastGameMove(gameId);
        if (lastGameMoveOptional.isEmpty()) {
            log.info("Game {} has no moves yet!", gameId);

            if (instanceName.equals(currentGame.getFirstTurn())) {
                executeFirstMove(gameId);
            }
            return;
        }

        PlayerRole nextMoveRole = lastGameMoveOptional.get().getNextMoveRole();
        PlayerRole currentRole = instanceRoleService.getCurrentRole();

        if (nextMoveRole == currentRole) {
            executeNextMove(currentRole, lastGameMoveOptional.get());
        }


    }

    private void executeNextMove(PlayerRole currentRole, GameMove lastGameMove) {
        String prevBoardState = lastGameMove.getCurrentBoardState();
        String currentBoardState = insertRandomlySymbol(prevBoardState, currentRole);
        GameMove nextGameMove = buildNextGameMove(lastGameMove, currentBoardState, currentRole);
        gameMoveKafkaProducer.sendMessage(nextGameMove);
    }



    private void executeFirstMove(UUID gameId) {
        // come up with a first move and send the message to Kafka
        GameMove gameMove = buildFirstGameMove(gameId);
        gameMoveKafkaProducer.sendMessage(gameMove);
    }

    private GameMove buildNextGameMove(
            GameMove lastGameMove,
            String currentBoardState,
            PlayerRole currentRole
    ) {
        return GameMove.builder()
                .gameId(lastGameMove.getGameId())
                .prevBoardState(lastGameMove.getCurrentBoardState())
                .currentBoardState(currentBoardState)
                .moveNumber(lastGameMove.getMoveNumber() + 1)
                .currentPlayerRole(currentRole)
                // todo - create winner determination logic and fill nextMoveRole properly
                .nextMoveRole(currentRole.other())
                .currentInstance(instanceName)
                .build();
    }

    // todo - somehow avoid code duplication between buildFirstGameMove and buildNextGameMove
    private GameMove buildFirstGameMove(UUID gameId) {
        String initialBoardState = "---------";
        String currentBoardState = insertRandomlySymbol(initialBoardState, PlayerRole.X);
        return GameMove.builder()
                .gameId(gameId)
                .prevBoardState(initialBoardState)
                .currentBoardState(currentBoardState)
                .moveNumber(1)
                .currentPlayerRole(PlayerRole.X)
                .nextMoveRole(PlayerRole.O)
                .currentInstance(instanceName)
                .build();
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

    private static List<Integer> getEmptyBoardPositions(String boardState) {
        List<Integer> dashPositions = new ArrayList<>();

        for (int i = 0; i < boardState.length(); i++) {
            if (boardState.charAt(i) == '-') {
                dashPositions.add(i);
            }
        }
        return dashPositions;
    }
}
