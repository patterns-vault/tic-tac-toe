package com.scentbird.tictactoe.tictactoe.dto;

import lombok.*;

import java.util.List;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    private List<String> board;
    private String status;
}
