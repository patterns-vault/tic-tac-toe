package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.GameStatus;
import com.scentbird.tictactoe.tictactoe.service.EndGameDeterminationService;
import com.scentbird.tictactoe.tictactoe.service.EndGameService;
import com.scentbird.tictactoe.tictactoe.service.GameMoveService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameMoveKafkaConsumer {

    private final GameMoveService gameMoveService;
    private final EndGameDeterminationService endGameDeterminationService;
    private final EndGameService endGameService;

    @Value("${tic-tac-toe.move.delay}")
    private Integer moveDelay;

    @KafkaListener(
            topics = "${tic-tac-toe.kafka.game-move.topic.name}",
            groupId = "${spring.kafka.consumer.game-move.group-id}",
            containerFactory = "gameMoveKafkaListenerContainerFactory"
    )
    public void consume(GameMove gameMove) {
        log.info("Consumed GameMove: {}", gameMove);
        String currentBoardState = gameMove.getCurrentBoardState();
        gameMoveService.saveGameMove(gameMove);
        EndGameStatus endGameStatus = endGameDeterminationService.shouldGameEnd(currentBoardState);

        if (endGameStatus.getGameStatus() == GameStatus.FINISHED) {
            printGameIsOver(currentBoardState);
            pause();
            endGameService.persistEndGame(gameMove, endGameStatus);
        }
    }

    // todo - replace with WebSocket or RSocket in the future
    // it's actually a hack so that UI can read the last move before the game's status
    // becomes FINISHED.
    @SneakyThrows
    private void pause() {
        Thread.sleep(moveDelay / 2);
    }

    private void printGameIsOver(String currentBoardState) {
        log.info("It's GAME OVER!!! {}", currentBoardState);
    }

}
