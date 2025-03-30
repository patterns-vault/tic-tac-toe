package com.scentbird.tictactoe.tictactoe.controller;

import com.scentbird.tictactoe.tictactoe.dto.GameBoardDto;
import com.scentbird.tictactoe.tictactoe.service.GameMoveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game-state")
@Slf4j
@RequiredArgsConstructor
public class GameStateController {

    private final GameMoveService gameMoveService;

    @GetMapping
    public GameBoardDto getGameState() {
        return GameBoardDto.builder()
                .boardState(gameMoveService.getLatestBoardState())
                .build();
    }

}
