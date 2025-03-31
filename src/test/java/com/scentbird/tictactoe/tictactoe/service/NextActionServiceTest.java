package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.kafka.GameMoveKafkaProducer;
import com.scentbird.tictactoe.tictactoe.model.Game;
import com.scentbird.tictactoe.tictactoe.model.GameMove;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NextActionServiceTest {

    @Mock
    private GameService gameService;

    @Mock
    private FirstTurnClaimService firstTurnClaimService;

    @Mock
    private GameMoveService gameMoveService;

    @Mock
    private GameMoveKafkaProducer gameMoveKafkaProducer;

    @Mock
    private InstanceRoleService instanceRoleService;

    @InjectMocks
    private NextActionService nextActionService;

    private static final String INSTANCE_NAME = "test-instance";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nextActionService = new NextActionService(gameService, firstTurnClaimService, gameMoveService, gameMoveKafkaProducer, instanceRoleService);
        ReflectionTestUtils.setField(nextActionService, "instanceName", INSTANCE_NAME);
    }

    @Test
    void testExecuteNextAction_NoActiveGames() {
        // Arrange
        when(gameService.getMostRecentUnfinishedGame()).thenReturn(Optional.empty());

        // Act
        nextActionService.executeNextAction();

        // Assert
        verify(firstTurnClaimService, times(1)).initiateFirstTurnClaim();
        verify(gameService, times(1)).getMostRecentUnfinishedGame();
        verifyNoInteractions(gameMoveService, gameMoveKafkaProducer);
    }

    @Test
    void testExecuteNextAction_NoMovesInGame_ThisInstanceFirstTurn() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder()
                .gameId(gameId)
                .firstTurn(INSTANCE_NAME)
                .build();

        when(gameService.getMostRecentUnfinishedGame()).thenReturn(Optional.of(game));
        when(gameMoveService.getLastGameMove(gameId)).thenReturn(Optional.empty()); // No moves

        // Mocking role retrieval
        when(instanceRoleService.getCurrentRole()).thenReturn(PlayerRole.X);

        // Act
        nextActionService.executeNextAction();

        // Assert
        ArgumentCaptor<GameMove> captor = ArgumentCaptor.forClass(GameMove.class);
        verify(gameMoveKafkaProducer, times(1)).sendMessage(captor.capture());

        // Verify GameMove properties
        GameMove sentMove = captor.getValue();
        assertEquals(gameId, sentMove.getGameId());
        assertEquals("---------", sentMove.getPrevBoardState());
        assertEquals(INSTANCE_NAME, sentMove.getCurrentInstance());
        assertEquals(PlayerRole.X, sentMove.getCurrentPlayerRole());
        assertEquals(PlayerRole.O, sentMove.getNextMoveRole());
        assertNotNull(sentMove.getCurrentBoardState()); // Ensure board state changed
        assertEquals(1, sentMove.getMoveNumber());
    }


    @Test
    void testExecuteNextAction_NoMovesInGame_NotThisInstanceFirstTurn() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder()
                .gameId(gameId)
                .firstTurn("another-not-this-test-instance")
                .build();
        when(gameService.getMostRecentUnfinishedGame()).thenReturn(Optional.of(game));
        when(gameMoveService.getLastGameMove(gameId)).thenReturn(Optional.empty()); // No moves

        // Act
        nextActionService.executeNextAction();

        // Assert
        verify(gameMoveKafkaProducer, times(0)).sendMessage(any(GameMove.class));
    }

    @Test
    void testExecuteNextAction_ThisInstanceMoveTurn() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder()
                .gameId(gameId)
                .firstTurn(INSTANCE_NAME)
                .build();

        GameMove lastMove = GameMove.builder()
                .gameId(gameId)
                .prevBoardState("---------")
                .currentBoardState("X--------")
                .moveNumber(1)
                .currentPlayerRole(PlayerRole.X)
                .nextMoveRole(PlayerRole.O)
                .currentInstance(INSTANCE_NAME)
                .build();

        when(gameService.getMostRecentUnfinishedGame()).thenReturn(Optional.of(game));
        when(gameMoveService.getLastGameMove(game.getGameId())).thenReturn(Optional.of(lastMove));
        when(instanceRoleService.getCurrentRole()).thenReturn(PlayerRole.O);

        // Act
        nextActionService.executeNextAction();

        // Assert
        ArgumentCaptor<GameMove> captor = ArgumentCaptor.forClass(GameMove.class);
        verify(gameMoveKafkaProducer, times(1)).sendMessage(captor.capture());

        // Verify GameMove properties
        GameMove sentMove = captor.getValue();
        assertEquals(gameId, sentMove.getGameId());
        assertEquals("X--------", sentMove.getPrevBoardState());
        assertEquals(PlayerRole.O, sentMove.getCurrentPlayerRole());
        assertEquals(PlayerRole.X, sentMove.getNextMoveRole());
        assertEquals(INSTANCE_NAME, sentMove.getCurrentInstance());
        assertNotNull(sentMove.getCurrentBoardState()); // Ensure board state changed
        assertEquals(2, sentMove.getMoveNumber());
    }

    @Test
    void testExecuteNextAction_NotThisInstanceMoveTurn() {
        // Arrange
        UUID gameId = UUID.randomUUID();
        Game game = Game.builder()
                .gameId(gameId)
                .firstTurn(INSTANCE_NAME)
                .build();
        GameMove lastMove = GameMove.builder()
                .gameId(gameId)
                .prevBoardState("---------")
                .currentBoardState("X--------")
                .moveNumber(1)
                .currentPlayerRole(PlayerRole.X)
                .nextMoveRole(PlayerRole.O) // Assume different role after the move
                .currentInstance(INSTANCE_NAME)
                .build();

        when(gameService.getMostRecentUnfinishedGame()).thenReturn(Optional.of(game));
        when(gameMoveService.getLastGameMove(game.getGameId())).thenReturn(Optional.of(lastMove));
        when(instanceRoleService.getCurrentRole()).thenReturn(PlayerRole.X); // Different role

        // Act
        nextActionService.executeNextAction();

        // Assert
        verify(gameMoveKafkaProducer, times(0)).sendMessage(any(GameMove.class));
    }
}
