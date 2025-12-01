package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UpdateVenueUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateVenueUseCase - Unit Tests")
class UpdateVenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private UpdateVenueUseCaseImpl updateVenueUseCase;

    private Venue existingVenue;
    private Venue updateData;

    @BeforeEach
    void setUp() {
        existingVenue = new Venue();
        existingVenue.setId(1L);
        existingVenue.setName("Teatro Original");
        existingVenue.setLocation("Bogotá");
        existingVenue.setCapacity(1000);
        existingVenue.setDescription("Descripción original");
        existingVenue.setActive(true);

        updateData = new Venue();
        updateData.setName("Teatro Actualizado");
        updateData.setLocation("Medellín");
        updateData.setCapacity(2000);
        updateData.setDescription("Nueva descripción");
        updateData.setActive(false);
    }

    @Test
    @DisplayName("Should update venue successfully when all validations pass")
    void shouldUpdateVenueSuccessfully() {
        // Given
        when(venueRepositoryPort.findById(1L)).thenReturn(Optional.of(existingVenue));
        when(venueRepositoryPort.save(any(Venue.class))).thenReturn(existingVenue);

        // When
        Venue updatedVenue = updateVenueUseCase.execute(1L, updateData);

        // Then
        assertNotNull(updatedVenue);
        verify(venueRepositoryPort, times(1)).findById(1L);
        verify(venueRepositoryPort, times(1)).save(any(Venue.class));
    }

    @Test
    @DisplayName("Should throw exception when venue does not exist")
    void shouldThrowExceptionWhenVenueNotFound() {
        // Given
        when(venueRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateVenueUseCase.execute(999L, updateData)
        );

        assertEquals("Venue con ID 999 no encontrado", exception.getMessage());
        verify(venueRepositoryPort, times(1)).findById(999L);
        verify(venueRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should update only changed fields")
    void shouldUpdateOnlyChangedFields() {
        // Given
        Venue partialUpdate = new Venue();
        partialUpdate.setName("Nuevo Nombre");
        // Solo actualizamos el nombre

        when(venueRepositoryPort.findById(1L)).thenReturn(Optional.of(existingVenue));
        when(venueRepositoryPort.save(any(Venue.class))).thenReturn(existingVenue);

        // When
        Venue updatedVenue = updateVenueUseCase.execute(1L, partialUpdate);

        // Then
        assertNotNull(updatedVenue);
        verify(venueRepositoryPort, times(1)).save(any(Venue.class));
    }
}