package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.CreateEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;

import java.time.LocalDateTime;

/**
 * Implementación del caso de uso: Crear Evento.
 *
 * Contiene toda la lógica de negocio para crear un nuevo evento.
 * No tiene dependencias de Spring ni JPA.
 */
public class CreateEventUseCaseImpl implements CreateEventUseCase {

    private final EventRepositoryPort eventRepository;
    private final VenueRepositoryPort venueRepository;

    /**
     * Constructor que inyecta las dependencias (puertos).
     * La inyección se configura en BeanConfiguration.
     */
    public CreateEventUseCaseImpl(EventRepositoryPort eventRepository,
                                  VenueRepositoryPort venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public Event execute(Event event) {
        // REGLA 1: El nombre no puede estar vacío
        validateEventName(event.getName());

        // REGLA 2: No puede existir otro evento con el mismo nombre
        if (eventRepository.existsByNameIgnoreCase(event.getName())) {
            throw new IllegalArgumentException(
                    "Ya existe un evento con el nombre: " + event.getName()
            );
        }

        // REGLA 3: Si tiene venueId, el venue debe existir
        if (event.getVenueId() != null) {
            validateVenueExists(event.getVenueId());
        }

        // REGLA 4: La fecha del evento debe ser futura
        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento debe ser futura");
        }

        // REGLA 5: La capacidad debe ser positiva
        if (event.getCapacity() != null && event.getCapacity() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }

        // REGLA 6: El precio no puede ser negativo
        if (event.getTicketPrice() != null && event.getTicketPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }

        // Si todo esta bien, guardar el evento
        return eventRepository.save(event);
    }

    // ========== MÉTODOS PRIVADOS DE VALIDACIÓN ==========

    private void validateEventName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del evento no puede estar vacío");
        }
    }

    private void validateVenueExists(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new IllegalArgumentException("El venue con ID " + venueId + " no existe");
        }
    }
}