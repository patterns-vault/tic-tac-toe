package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.InstanceRole;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import com.scentbird.tictactoe.tictactoe.repo.InstanceRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstanceRoleService {

    private final InstanceRoleRepo instanceRoleRepo;

    @Value("${spring.application.name}")
    private String instanceName;

    public void save(InstanceRole instanceRole) {
        instanceRoleRepo.save(instanceRole);
    }

    public PlayerRole getCurrentRole() {
        InstanceRole instanceRole = instanceRoleRepo.getInstanceRoleByInstance(instanceName).orElseThrow(
                () -> new IllegalStateException("No instance role found for " + instanceName)
        );
        return instanceRole.getRole();
    }

    public void deleteAll() {
        instanceRoleRepo.deleteAll();
    }
}
