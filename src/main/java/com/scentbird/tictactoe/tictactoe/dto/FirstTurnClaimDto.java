package com.scentbird.tictactoe.tictactoe.dto;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirstTurnClaimDto {
    private String uuid;
    private String instanceName;
    @Builder.Default
    private Instant timestamp = Instant.now();
}
