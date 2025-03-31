package com.scentbird.tictactoe.tictactoe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class ScheduledJobTest {

    @Mock
    private NextActionService nextActionService;

    @InjectMocks
    private ScheduledJob scheduledJob;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchDataFromDatabase() {
        // Given
        // No input parameters, just interaction verification

        // When
        scheduledJob.fetchDataFromDatabase();

        // Then
        verify(nextActionService).executeNextAction();
    }

}
