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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CreateVenueUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateVenueUseCase - Unit Tests")
class CreateVenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private CreateVenueUseCaseImpl createVenueUseCase;

    private Venue validVenue;

    @BeforeEach
    void setUp() {
        validVenue = new Venue();
        validVenue.setName("Teatro Colón");
        validVenue.setLocation("Bogotá, Colombia");
        validVenue.setCapacity(3000);
        validVenue.setDescription("Teatro moderno");
        validVenue.setActive(true);
    }

    @Test
    @DisplayName("Should create venue successfully when all validations pass")
    void shouldCreateVenueSuccessfully() {
        // Given
        when(venueRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.save(any(Venue.class))).thenReturn(validVenue);

        // When
        Venue createdVenue = createVenueUseCase.execute(validVenue);

        // Then
        assertNotNull(createdVenue);
        assertEquals("Teatro Colón", createdVenue.getName());
        assertEquals("Bogotá, Colombia", createdVenue.getLocation());
        verify(venueRepositoryPort, times(1)).existsByNameIgnoreCase("Teatro Colón");
        verify(venueRepositoryPort, times(1)).save(validVenue);
    }

    @Test
    @DisplayName("Should throw exception when venue name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Given
        validVenue.setName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createVenueUseCase.execute(validVenue)
        );

        assertEquals("El nombre del venue no puede estar vacío", exception.getMessage());
        verify(venueRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when venue name is duplicate")
    void shouldThrowExceptionWhenNameIsDuplicate() {
        // Given
        when(venueRepositoryPort.existsByNameIgnoreCase("Teatro Colón")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createVenueUseCase.execute(validVenue)
        );

        assertEquals("Ya existe un venue con el nombre: Teatro Colón", exception.getMessage());
        verify(venueRepositoryPort, times(1)).existsByNameIgnoreCase("Teatro Colón");
        verify(venueRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when location is empty")
    void shouldThrowExceptionWhenLocationIsEmpty() {
        // Given
        validVenue.setLocation("");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createVenueUseCase.execute(validVenue)
        );

        assertEquals("La ubicación del venue no puede estar vacía", exception.getMessage());
        verify(venueRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when capacity is not positive")
    void shouldThrowExceptionWhenCapacityIsNotPositive() {
        // Given
        validVenue.setCapacity(0);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createVenueUseCase.execute(validVenue)
        );

        assertEquals("La capacidad debe ser mayor a 0", exception.getMessage());
        verify(venueRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should create venue with null description")
    void shouldCreateVenueWithNullDescription() {
        // Given
        validVenue.setDescription(null);
        when(venueRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.save(any(Venue.class))).thenReturn(validVenue);

        // When
        Venue createdVenue = createVenueUseCase.execute(validVenue);

        // Then
        assertNotNull(createdVenue);
        assertNull(createdVenue.getDescription());
        verify(venueRepositoryPort, times(1)).save(validVenue);
    }
}