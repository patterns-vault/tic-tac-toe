package com.scentbird.tictactoe.tictactoe.repo;

import com.scentbird.tictactoe.tictactoe.model.InstanceRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstanceRoleRepo extends JpaRepository<InstanceRole, Long> {
    Optional<InstanceRole> getInstanceRoleByInstance(String instance);
}
