package com.scentbird.tictactoe.tictactoe;

import com.scentbird.tictactoe.tictactoe.dto.GameBoardDto;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Stream;

//@SpringBootTest
class TicTacToeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void randomizerTest() {
        System.out.println("Go!");
        System.out.println("=================================");
        System.out.println();

        for (int i = 0; i < 10; i++) {
            long mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
            System.out.println(mostSignificantBits);
        }

        String uuid = UUID.randomUUID().toString();

        long mostSignificantBits = UUID.fromString(uuid).getMostSignificantBits();
        System.out.println("One more");
        System.out.println(mostSignificantBits);

        double sqrt = Math.sqrt(-7);

    }


    @Test
    void sqrtRootPlayground() {
        long mostSignificantBits = UUID.randomUUID().getMostSignificantBits();
        double sqrt = Math.sqrt(Math.abs(mostSignificantBits));

        double fractionalPart = sqrt - Math.floor(sqrt);

        System.out.println("===================================");
        System.out.println(fractionalPart);
        System.out.println("===================================");
    }

    @Test
    void boardPlayGround() {
        String initialBoardState = "---------";
        String currentBoardState = insertRandomX(initialBoardState);
        System.out.println(currentBoardState);
    }

    private String insertRandomX(String input) {
        List<Integer> dashPositions = new ArrayList<>();

        // Collect positions of dashes
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '-') {
                dashPositions.add(i);
            }
        }

        if (dashPositions.isEmpty()) {
            return input; // No dashes to replace
        }

        // Choose a random dash position
        Random random = new Random();
        int randomIndex = random.nextInt(dashPositions.size());
        int positionToReplace = dashPositions.get(randomIndex);

        // Replace the selected dash with 'X'
        return input.substring(0, positionToReplace) + "X" + input.substring(positionToReplace + 1);
    }

    @Test
    void stackPlayground() {
        char c = PlayerRole.X.name().charAt(0);
        System.out.println(c);
        System.out.println();

    }

    private GameBoardDto buildGameBoardDto(String boardState) {
        return GameBoardDto.builder()
                .boardState(boardState)
                .build();
    }

}
