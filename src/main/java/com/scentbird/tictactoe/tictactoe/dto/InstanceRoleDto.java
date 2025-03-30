package com.scentbird.tictactoe.tictactoe.dto;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceRoleDto {
    private String instance;
    private String role;
}
