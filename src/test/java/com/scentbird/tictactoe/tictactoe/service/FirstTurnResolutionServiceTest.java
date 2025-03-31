package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import com.scentbird.tictactoe.tictactoe.model.InstanceRole;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class FirstTurnResolutionServiceTest {

    @Mock
    private StartGameService startGameService;

    @Mock
    private InstanceRoleService instanceRoleService;

    @InjectMocks
    private FirstTurnResolutionService firstTurnResolutionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testResolveClaims_singleClaim_noResolution() {
        // Given: A single claim
        String instanceName = "instance1";
        String uuid = UUID.randomUUID().toString();
        FirstTurnClaimDto claim = FirstTurnClaimDto.builder()
                .instanceName(instanceName)
                .uuid(uuid)
                .build();

        // When: resolveClaims is called with one claim
        firstTurnResolutionService.resolveClaims(claim);

        // Then: No resolution should occur
        verify(instanceRoleService, never()).deleteAll();
        verify(instanceRoleService, never()).save(any());
        verify(startGameService, never()).startGame(anyString(), anyString());
    }

    @Test
    void testResolveClaims_twoClaims_resolvesFirstTurn() {
        // Given: Two claims with deterministically different fractional parts
        String uuid1 = "00000000-0000-0000-0000-000000000001";
        String uuid2 = "00000000-0000-0000-0000-000000000002";

        String instanceName1 = "instance1";
        String instanceName2 = "instance2";


        FirstTurnClaimDto claim1 = FirstTurnClaimDto.builder()
                        .instanceName(instanceName1)
                        .uuid(uuid1)
                        .build();

        FirstTurnClaimDto claim2 = FirstTurnClaimDto.builder()
                .instanceName(instanceName2)
                .uuid(uuid2)
                .build();


        // When: resolveClaims is called for both claims
        firstTurnResolutionService.resolveClaims(claim1);
        firstTurnResolutionService.resolveClaims(claim2);

        // Then: The roles for the two instances should be resolved
        verify(instanceRoleService).deleteAll();

        // Verify the first instance gets PlayerRole.X and the second gets PlayerRole.O
        ArgumentCaptor<InstanceRole> captor = ArgumentCaptor.forClass(InstanceRole.class);
        verify(instanceRoleService, times(2)).save(captor.capture());

        InstanceRole firstRole = captor.getAllValues().get(0);
        InstanceRole secondRole = captor.getAllValues().get(1);

        assertEquals(instanceName2, firstRole.getInstance());
        assertEquals(PlayerRole.X, firstRole.getRole());

        assertEquals(instanceName1, secondRole.getInstance());
        assertEquals(PlayerRole.O, secondRole.getRole());

        // Verify the game is started with the correct instance
        verify(startGameService).startGame(eq(claim2.getUuid()), eq(instanceName2));
    }
}
