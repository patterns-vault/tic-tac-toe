package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import com.scentbird.tictactoe.tictactoe.service.FirstTurnResolutionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FirstTurnClaimKafkaConsumerTest {

    @Mock
    private FirstTurnResolutionService firstTurnResolutionService;

    @InjectMocks
    private FirstTurnClaimKafkaConsumer firstTurnClaimKafkaConsumer;

    @Test
    void testConsume_shouldInvokeResolutionService() {
        // Arrange
        FirstTurnClaimDto mockDto = new FirstTurnClaimDto();

        // Act
        firstTurnClaimKafkaConsumer.consume(mockDto);

        // Assert
        verify(firstTurnResolutionService, times(1)).resolveClaims(mockDto);
    }
}
