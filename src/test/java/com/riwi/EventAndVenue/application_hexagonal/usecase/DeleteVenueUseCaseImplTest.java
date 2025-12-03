package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
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
 * Pruebas unitarias para DeleteVenueUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteVenueUseCase - Unit Tests")
class DeleteVenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @Mock
    private Counter venuesDeletedCounter;

    @InjectMocks
    private DeleteVenueUseCaseImpl deleteVenueUseCase;

    @Test
    @DisplayName("Should delete venue successfully when venue exists")
    void shouldDeleteVenueSuccessfully() {
        // Given
        Long venueId = 1L;
        when(venueRepositoryPort.existsById(venueId)).thenReturn(true);
        doNothing().when(venueRepositoryPort).deleteById(venueId);

        // When
        assertDoesNotThrow(() -> deleteVenueUseCase.execute(venueId));

        // Then
        verify(venueRepositoryPort, times(1)).existsById(venueId);
        verify(venueRepositoryPort, times(1)).deleteById(venueId);
    }

    @Test
    @DisplayName("Should throw exception when venue does not exist")
    void shouldThrowExceptionWhenVenueNotFound() {
        // Given
        Long venueId = 999L;
        when(venueRepositoryPort.existsById(venueId)).thenReturn(false);

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> deleteVenueUseCase.execute(venueId)
        );

        assertEquals("Venue not found with id: 999", exception.getMessage());
        verify(venueRepositoryPort, times(1)).existsById(venueId);
        verify(venueRepositoryPort, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should verify venue existence before deletion")
    void shouldVerifyVenueExistenceBeforeDeletion() {
        // Given
        Long venueId = 5L;
        when(venueRepositoryPort.existsById(venueId)).thenReturn(true);

        // When
        deleteVenueUseCase.execute(venueId);

        // Then
        verify(venueRepositoryPort, times(1)).existsById(venueId);
        verify(venueRepositoryPort, times(1)).deleteById(venueId);
    }
}