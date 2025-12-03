package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.micrometer.core.instrument.Counter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para DeleteEventUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteEventUseCase - Unit Tests")
class DeleteEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @Mock
    private Counter eventsDeletedCounter;

    @InjectMocks
    private DeleteEventUseCaseImpl deleteEventUseCase;

    @Test
    @DisplayName("Should delete event successfully when event exists")
    void shouldDeleteEventSuccessfully() {
        // Given
        Long eventId = 1L;
        when(eventRepositoryPort.existsById(eventId)).thenReturn(true);
        doNothing().when(eventRepositoryPort).deleteById(eventId);

        // When
        assertDoesNotThrow(() -> deleteEventUseCase.execute(eventId));

        // Then
        verify(eventRepositoryPort, times(1)).existsById(eventId);
        verify(eventRepositoryPort, times(1)).deleteById(eventId);
    }

    @Test
    @DisplayName("Should throw exception when event does not exist")
    void shouldThrowExceptionWhenEventNotFound() {
        // Given
        Long eventId = 999L;
        when(eventRepositoryPort.existsById(eventId)).thenReturn(false);

        // When & Then
        jakarta.persistence.EntityNotFoundException exception = assertThrows(
                jakarta.persistence.EntityNotFoundException.class,
                () -> deleteEventUseCase.execute(eventId)
        );

        assertEquals("Event not found with id: 999", exception.getMessage());
        verify(eventRepositoryPort, times(1)).existsById(eventId);
        verify(eventRepositoryPort, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should verify event existence before deletion")
    void shouldVerifyEventExistenceBeforeDeletion() {
        // Given
        Long eventId = 5L;
        when(eventRepositoryPort.existsById(eventId)).thenReturn(true);

        // When
        deleteEventUseCase.execute(eventId);

        // Then
        verify(eventRepositoryPort, times(1)).existsById(eventId);
        verify(eventRepositoryPort, times(1)).deleteById(eventId);
    }
}