package com.scentbird.tictactoe.tictactoe.service;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import com.scentbird.tictactoe.tictactoe.kafka.FirstTurnClaimKafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FirstTurnClaimServiceTest {

    private FirstTurnClaimKafkaProducer firstTurnClaimKafkaProducer;
    private FirstTurnClaimService firstTurnClaimService;

    @BeforeEach
    void setUp() {
        firstTurnClaimKafkaProducer = Mockito.mock(FirstTurnClaimKafkaProducer.class);
        firstTurnClaimService = new FirstTurnClaimService(firstTurnClaimKafkaProducer);

        // Set the value of the instanceName field
        ReflectionTestUtils.setField(firstTurnClaimService, "instanceName", "TestInstance");
    }

    @Test
    void testInitiateFirstTurnClaim() {
        // Act
        firstTurnClaimService.initiateFirstTurnClaim();

        // Capture the message sent to the Kafka producer
        ArgumentCaptor<FirstTurnClaimDto> captor = ArgumentCaptor.forClass(FirstTurnClaimDto.class);
        verify(firstTurnClaimKafkaProducer, times(1)).sendMessage(captor.capture());
        FirstTurnClaimDto capturedDto = captor.getValue();

        // Assert the captured value
        assertEquals("TestInstance", capturedDto.getInstanceName());
        assertEquals(36, capturedDto.getUuid().length()); // UUID length should be 36 characters
    }
}
