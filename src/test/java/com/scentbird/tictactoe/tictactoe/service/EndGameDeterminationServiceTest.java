package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.EndGameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndGameDeterminationServiceTest {

    @Test
    void test() {
        // XOOOXXOXX

        String boardState = "XOOOXXOXX";
        EndGameDeterminationService endGameDeterminationService = new EndGameDeterminationService();
        EndGameStatus endGameStatus = endGameDeterminationService.shouldGameEnd(boardState);

        System.out.println(endGameStatus);
        System.out.println();
    }

}
