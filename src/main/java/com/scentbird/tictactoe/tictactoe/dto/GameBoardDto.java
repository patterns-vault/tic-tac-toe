package com.scentbird.tictactoe.tictactoe.dto;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameBoardDto {
    private String boardState;
}
