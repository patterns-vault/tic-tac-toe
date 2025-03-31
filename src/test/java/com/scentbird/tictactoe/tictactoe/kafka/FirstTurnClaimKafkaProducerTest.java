package com.scentbird.tictactoe.tictactoe.kafka;

import com.scentbird.tictactoe.tictactoe.dto.FirstTurnClaimDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FirstTurnClaimKafkaProducerTest {

    private static final String TEST_TOPIC = "test-topic";

    @Mock
    private KafkaTemplate<String, FirstTurnClaimDto> kafkaTemplate;

    private FirstTurnClaimKafkaProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producer = new FirstTurnClaimKafkaProducer(kafkaTemplate);
        ReflectionTestUtils.setField(producer, "firstTurnClaimTopicName", TEST_TOPIC);
    }

    @Test
    void testSendMessage() {
        // Arrange
        FirstTurnClaimDto mockDto = new FirstTurnClaimDto();
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<FirstTurnClaimDto> dtoCaptor = ArgumentCaptor.forClass(FirstTurnClaimDto.class);

        // Act
        producer.sendMessage(mockDto);

        // Assert
        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), dtoCaptor.capture());
        assertEquals(TEST_TOPIC, topicCaptor.getValue());
        assertEquals(mockDto, dtoCaptor.getValue());
    }
}
