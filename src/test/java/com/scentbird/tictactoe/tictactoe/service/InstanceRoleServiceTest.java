package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.model.InstanceRole;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import com.scentbird.tictactoe.tictactoe.repo.InstanceRoleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InstanceRoleServiceTest {

    @Mock
    private InstanceRoleRepo instanceRoleRepo;

    @InjectMocks
    private InstanceRoleService instanceRoleService;

    private static final String INSTANCE_NAME = "TestInstance";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        instanceRoleService = new InstanceRoleService(instanceRoleRepo);

        // Set the value of the instanceName field
        ReflectionTestUtils.setField(instanceRoleService, "instanceName", "TestInstance");
    }

    @Test
    public void testSave() {
        // Arrange
        InstanceRole instanceRole = new InstanceRole();
        // Act
        instanceRoleService.save(instanceRole);
        // Assert
        verify(instanceRoleRepo, times(1)).save(instanceRole);
    }

    @Test
    public void testGetCurrentRole_Success() {
        // Arrange
        InstanceRole instanceRole = new InstanceRole();
        instanceRole.setRole(PlayerRole.X); // Assume PlayerRole.X is an enum or class
        instanceRole.setInstance(INSTANCE_NAME);

        when(instanceRoleRepo.getInstanceRoleByInstance(INSTANCE_NAME)).thenReturn(Optional.of(instanceRole));

        // Act
        PlayerRole role = instanceRoleService.getCurrentRole();

        // Assert
        assertEquals(PlayerRole.X, role); // Validate the returned role is correct
        verify(instanceRoleRepo, times(1)).getInstanceRoleByInstance(INSTANCE_NAME);
    }

    @Test
    public void testGetCurrentRole_NoInstanceRoleFound() {
        // Arrange
        when(instanceRoleRepo.getInstanceRoleByInstance(INSTANCE_NAME)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            instanceRoleService.getCurrentRole();
        });

        assertEquals("No instance role found for " + INSTANCE_NAME, exception.getMessage());
        verify(instanceRoleRepo, times(1)).getInstanceRoleByInstance(INSTANCE_NAME);
    }

    @Test
    public void testDeleteAll() {
        // Act
        instanceRoleService.deleteAll();
        // Assert
        verify(instanceRoleRepo, times(1)).deleteAll();
    }
}
