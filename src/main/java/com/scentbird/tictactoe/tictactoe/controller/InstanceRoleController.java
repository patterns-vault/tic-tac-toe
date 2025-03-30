package com.scentbird.tictactoe.tictactoe.controller;

import com.scentbird.tictactoe.tictactoe.dto.InstanceRoleDto;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import com.scentbird.tictactoe.tictactoe.service.InstanceRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/instance-role")
@Slf4j
@RequiredArgsConstructor
public class InstanceRoleController {

    private final InstanceRoleService instanceRoleService;

    @Value("${spring.application.name}")
    private String instanceName;

    @GetMapping
    public InstanceRoleDto getGameState() {
        PlayerRole currentRole = instanceRoleService.getCurrentRole();
        return InstanceRoleDto.builder()
                .instance(instanceName)
                .role(currentRole.name())
                .build();
    }
}
