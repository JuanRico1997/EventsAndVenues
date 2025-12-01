package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para UpdateEventUseCaseImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateEventUseCase - Unit Tests")
class UpdateEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private UpdateEventUseCaseImpl updateEventUseCase;

    private Event existingEvent;
    private Event updateData;

    @BeforeEach
    void setUp() {
        existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setName("Concierto Original");
        existingEvent.setDescription("Descripción original");
        existingEvent.setEventDate(LocalDateTime.now().plusDays(30));
        existingEvent.setVenueId(1L);
        existingEvent.setCapacity(500);
        existingEvent.setTicketPrice(30000.0);
        existingEvent.setActive(true);

        updateData = new Event();
        updateData.setName("Concierto Actualizado");
        updateData.setDescription("Nueva descripción");
        updateData.setEventDate(LocalDateTime.now().plusDays(60));
        updateData.setVenueId(2L);
        updateData.setCapacity(1000);
        updateData.setTicketPrice(50000.0);
        updateData.setActive(false);
    }

    @Test
    @DisplayName("Should update event successfully when all validations pass")
    void shouldUpdateEventSuccessfully() {
        // Given
        when(eventRepositoryPort.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(venueRepositoryPort.existsById(2L)).thenReturn(true);
        when(eventRepositoryPort.save(any(Event.class))).thenReturn(existingEvent);

        // When
        Event updatedEvent = updateEventUseCase.execute(1L, updateData);

        // Then
        assertNotNull(updatedEvent);
        verify(eventRepositoryPort, times(1)).findById(1L);
        verify(eventRepositoryPort, times(1)).save(any(Event.class));
    }

    @Test
    @DisplayName("Should throw exception when event does not exist")
    void shouldThrowExceptionWhenEventNotFound() {
        // Given
        when(eventRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateEventUseCase.execute(999L, updateData)
        );

        assertEquals("Evento con ID 999 no encontrado", exception.getMessage());
        verify(eventRepositoryPort, times(1)).findById(999L);
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when updating to non-existent venue")
    void shouldThrowExceptionWhenVenueNotFound() {
        // Given
        when(eventRepositoryPort.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(venueRepositoryPort.existsById(2L)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> updateEventUseCase.execute(1L, updateData)
        );

        assertEquals("El venue con ID 2 no existe", exception.getMessage());
        verify(eventRepositoryPort, times(1)).findById(1L);
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should update only changed fields")
    void shouldUpdateOnlyChangedFields() {
        // Given
        Event partialUpdate = new Event();
        partialUpdate.setName("Nuevo Nombre");
        // Solo actualizamos el nombre, los demás campos quedan null

        when(eventRepositoryPort.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepositoryPort.save(any(Event.class))).thenReturn(existingEvent);

        // When
        Event updatedEvent = updateEventUseCase.execute(1L, partialUpdate);

        // Then
        assertNotNull(updatedEvent);
        verify(eventRepositoryPort, times(1)).save(any(Event.class));
    }
}