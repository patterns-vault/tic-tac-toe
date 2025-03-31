package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import com.scentbird.tictactoe.tictactoe.model.InstanceRole;
import com.scentbird.tictactoe.tictactoe.model.PlayerRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirstTurnResolutionService {

    private final StartGameService startGameService;
    private final InstanceRoleService instanceRoleService;

    private Map<String, FirstTurnClaimDto> claims = new HashMap<>();

    public void resolveClaims(FirstTurnClaimDto firstTurnClaimDto) {
        claims.put(firstTurnClaimDto.getInstanceName(), firstTurnClaimDto);
        if (claims.keySet().size() == 2) {
            resolveFirstTurn();
        }
    }

    private void resolveFirstTurn() {
        Map<String, Double> instanceName2FractionalPart = new HashMap<>();
        claims.forEach((instanceName, claimDto) -> {
            instanceName2FractionalPart.put(
                    instanceName,
                    extractFractionalPart(claimDto.getUuid())
            );
        });

        String firstTurnInstanceName = instanceName2FractionalPart.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("First turn resolution failed"));

        log.info("firstTurnInstanceName: {}", firstTurnInstanceName);

        // the table should always have just two records - instance1 and instance2 data.
        // that's why here is a table cleanup
        instanceRoleService.deleteAll();

        instanceRoleService.save(
                InstanceRole.builder()
                        .instance(firstTurnInstanceName)
                        .role(PlayerRole.X)
                        .build()
        );

        claims.keySet().forEach(instanceName -> {
            if (!instanceName.equals(firstTurnInstanceName)) {
                instanceRoleService.save(
                        InstanceRole.builder()
                                .instance(instanceName)
                                .role(PlayerRole.O)
                                .build()
                );
            }
        });

        startGameService.startGame(
                claims.get(firstTurnInstanceName).getUuid(),
                firstTurnInstanceName
        );

        claims = new HashMap<>();
    }

    private double extractFractionalPart(String uuid) {
        long mostSignificantBits = UUID.fromString(uuid).getMostSignificantBits();
        double sqrt = Math.sqrt(Math.abs(mostSignificantBits));
        double fractionalPart = sqrt - Math.floor(sqrt);
        return fractionalPart;
    }
}
