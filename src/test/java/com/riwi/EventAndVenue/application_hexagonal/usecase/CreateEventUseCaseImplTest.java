package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import java.util.function.Supplier;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CreateEventUseCaseImpl.
 *
 * Se mockean los puertos de salida (repositorios) para aislar la lógica de negocio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateEventUseCase - Unit Tests")
class CreateEventUseCaseImplTest {

    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @Mock
    private Counter eventsCreatedCounter;  // ← AGREGAR

    @Mock
    private Timer eventCreationTimer;

    @InjectMocks
    private CreateEventUseCaseImpl createEventUseCase;

    private Event validEvent;
    private Venue validVenue;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        validVenue = new Venue();
        validVenue.setId(1L);
        validVenue.setName("Teatro Colón");
        validVenue.setLocation("Bogotá");
        validVenue.setCapacity(3000);
        validVenue.setActive(true);

        validEvent = new Event();
        validEvent.setName("Concierto Rock");
        validEvent.setDescription("Gran concierto de rock");
        validEvent.setEventDate(LocalDateTime.now().plusDays(30));
        validEvent.setVenueId(1L);
        validEvent.setCapacity(1000);
        validEvent.setTicketPrice(50000.0);
        validEvent.setActive(true);

        // Configurar el Timer mock para ejecutar el Supplier directamente
        when(eventCreationTimer.record(any(Supplier.class))).thenAnswer(invocation -> {
            Supplier<?> supplier = invocation.getArgument(0);
            return supplier.get();
        });
    }

    @Test
    @DisplayName("Should create event successfully when all validations pass")
    void shouldCreateEventSuccessfully() {
        // Given
        when(eventRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.existsById(1L)).thenReturn(true);
        when(eventRepositoryPort.save(any(Event.class))).thenReturn(validEvent);

        // When
        Event createdEvent = createEventUseCase.execute(validEvent);

        // Then
        assertNotNull(createdEvent);
        assertEquals("Concierto Rock", createdEvent.getName());
        verify(eventRepositoryPort, times(1)).existsByNameIgnoreCase("Concierto Rock");
        verify(venueRepositoryPort, times(1)).existsById(1L);
        verify(eventRepositoryPort, times(1)).save(validEvent);
    }

    @Test
    @DisplayName("Should throw exception when event name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Given
        validEvent.setName("");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEventUseCase.execute(validEvent)
        );

        assertEquals("El nombre del evento no puede estar vacío", exception.getMessage());
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when event name is duplicate")
    void shouldThrowExceptionWhenNameIsDuplicate() {
        // Given
        when(eventRepositoryPort.existsByNameIgnoreCase("Concierto Rock")).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEventUseCase.execute(validEvent)
        );

        assertEquals("Ya existe un evento con el nombre: Concierto Rock", exception.getMessage());
        verify(eventRepositoryPort, times(1)).existsByNameIgnoreCase("Concierto Rock");
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when venue does not exist")
    void shouldThrowExceptionWhenVenueNotFound() {
        // Given
        when(eventRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.existsById(1L)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEventUseCase.execute(validEvent)
        );

        assertEquals("El venue con ID 1 no existe", exception.getMessage());
        verify(venueRepositoryPort, times(1)).existsById(1L);
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when event date is in the past")
    void shouldThrowExceptionWhenDateIsInPast() {
        // Given
        when(eventRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.existsById(1L)).thenReturn(true);
        validEvent.setEventDate(LocalDateTime.now().minusDays(1));

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEventUseCase.execute(validEvent)
        );

        assertEquals("La fecha del evento debe ser futura", exception.getMessage());
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when capacity is not positive")
    void shouldThrowExceptionWhenCapacityIsNotPositive() {
        // Given
        when(eventRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.existsById(1L)).thenReturn(true);
        validEvent.setCapacity(0);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEventUseCase.execute(validEvent)
        );

        assertEquals("La capacidad debe ser mayor a 0", exception.getMessage());
        verify(eventRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when ticket price is negative")
    void shouldThrowExceptionWhenPriceIsNegative() {
        // Given
        when(eventRepositoryPort.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(venueRepositoryPort.existsById(1L)).thenReturn(true);
        validEvent.setTicketPrice(-100.0);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEventUseCase.execute(validEvent)
        );

        assertEquals("El precio no puede ser negativo", exception.getMessage());
        verify(eventRepositoryPort, never()).save(any());
    }
}