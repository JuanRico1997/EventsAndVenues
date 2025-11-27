package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.UpdateEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Implementación del caso de uso: Actualizar Evento.
 *
 * Contiene toda la lógica de negocio para actualizar un evento existente.
 */
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateEventUseCaseImpl.class);
    private final EventRepositoryPort eventRepository;
    private final VenueRepositoryPort venueRepository;

    public UpdateEventUseCaseImpl(EventRepositoryPort eventRepository,
                                  VenueRepositoryPort venueRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Transactional
    @Override
    public Event execute(Long id, Event eventData) {
        log.info("EVENT_UPDATE_START eventId={} eventName={}", id, eventData.getName());

        // REGLA 1: Verificar que el evento existe
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("EVENT_UPDATE_FAILED eventId={} reason=EventNotFound", id);
                    return new IllegalArgumentException("Evento con ID " + id + " no encontrado");
                });

        // REGLA 2: Actualizar nombre si se proporciona y validar
        if (eventData.getName() != null) {
            validateEventName(eventData.getName());

            // Verificar duplicados solo si el nombre cambió
            if (!eventData.getName().equalsIgnoreCase(existingEvent.getName())) {
                if (eventRepository.existsByNameIgnoreCase(eventData.getName())) {
                    log.error("EVENT_UPDATE_FAILED eventId={} eventName={} reason=DuplicateName",
                            id, eventData.getName());
                    throw new IllegalArgumentException(
                            "Ya existe un evento con el nombre: " + eventData.getName()
                    );
                }
            }

            existingEvent.setName(eventData.getName());
        }

        // REGLA 3: Actualizar descripción si se proporciona
        if (eventData.getDescription() != null) {
            existingEvent.setDescription(eventData.getDescription());
        }

        // REGLA 4: Actualizar fecha si se proporciona y validar
        if (eventData.getEventDate() != null) {
            if (eventData.getEventDate().isBefore(LocalDateTime.now())) {
                log.error("EVENT_UPDATE_FAILED eventId={} reason=PastDate", id);
                throw new IllegalArgumentException("La fecha del evento debe ser futura");
            }
            existingEvent.setEventDate(eventData.getEventDate());
        }

        // REGLA 5: Actualizar venueId si se proporciona y validar
        if (eventData.getVenueId() != null) {
            try {
                validateVenueExists(eventData.getVenueId());
                existingEvent.setVenueId(eventData.getVenueId());
            } catch (IllegalArgumentException e) {
                log.error("EVENT_UPDATE_FAILED eventId={} venueId={} reason=VenueNotFound",
                        id, eventData.getVenueId());
                throw e;
            }
        }

        // REGLA 6: Actualizar capacidad si se proporciona y validar
        if (eventData.getCapacity() != null) {
            if (eventData.getCapacity() <= 0) {
                log.error("EVENT_UPDATE_FAILED eventId={} capacity={} reason=InvalidCapacity",
                        id, eventData.getCapacity());
                throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
            }
            existingEvent.setCapacity(eventData.getCapacity());
        }

        // REGLA 7: Actualizar precio si se proporciona y validar
        if (eventData.getTicketPrice() != null) {
            if (eventData.getTicketPrice() < 0) {
                log.error("EVENT_UPDATE_FAILED eventId={} price={} reason=NegativePrice",
                        id, eventData.getTicketPrice());
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }
            existingEvent.setTicketPrice(eventData.getTicketPrice());
        }

        // REGLA 8: Actualizar estado activo si se proporciona
        if (eventData.getActive() != null) {
            existingEvent.setActive(eventData.getActive());
        }

        // Guardar los cambios
        Event updated = eventRepository.save(existingEvent);

        log.info("EVENT_UPDATE_SUCCESS eventId={} eventName={} venueId={}",
                updated.getId(), updated.getName(), updated.getVenueId());

        return updated;
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