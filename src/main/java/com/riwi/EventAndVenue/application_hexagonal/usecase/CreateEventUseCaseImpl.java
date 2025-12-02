package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.CreateEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Qualifier;


import java.time.LocalDateTime;


/**
 * Implementación del caso de uso: Crear Evento.

 * Contiene toda la lógica de negocio para crear un nuevo evento.
 * No tiene dependencias de Spring ni JPA.
 */
public class CreateEventUseCaseImpl implements CreateEventUseCase {


    private static final Logger log = LoggerFactory.getLogger(CreateEventUseCaseImpl.class);
    private final EventRepositoryPort eventRepository;
    private final VenueRepositoryPort venueRepository;
    private final Counter eventsCreatedCounter;
    private final Timer eventCreationTimer;



    /**
     * Constructor que inyecta las dependencias (puertos).
     * La inyección se configura en BeanConfiguration.
     */
    public CreateEventUseCaseImpl(EventRepositoryPort eventRepository,
                                  VenueRepositoryPort venueRepository,
                                  @Qualifier("eventsCreatedCounter") Counter eventsCreatedCounter,
                                  @Qualifier("eventCreationTimer") Timer eventCreationTimer) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventsCreatedCounter = eventsCreatedCounter;
        this.eventCreationTimer = eventCreationTimer;
    }

    @Transactional
    @Override
    public Event execute(Event event) {
        return eventCreationTimer.record(() -> {
            log.info("EVENT_CREATE_START eventName={} venueId={}", event.getName(), event.getVenueId());

            // REGLA 1: El nombre no puede estar vacío
            validateEventName(event.getName());

            // REGLA 2: No puede existir otro evento con el mismo nombre
            if (eventRepository.existsByNameIgnoreCase(event.getName())) {
                log.error("EVENT_CREATE_FAILED eventName={} reason=DuplicateName", event.getName());
                throw new IllegalArgumentException(
                        "Ya existe un evento con el nombre: " + event.getName()
                );
            }

            // REGLA 3: Si tiene venueId, el venue debe existir
            if (event.getVenueId() != null) {
                try {
                    validateVenueExists(event.getVenueId());
                } catch (IllegalArgumentException e) {
                    log.error("EVENT_CREATE_FAILED eventName={} venueId={} reason=VenueNotFound",
                            event.getName(), event.getVenueId());
                    throw e;
                }
            }

            // REGLA 4: La fecha del evento debe ser futura
            if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now())) {
                log.error("EVENT_CREATE_FAILED eventName={} eventDate={} reason=PastDate",
                        event.getName(), event.getEventDate());
                throw new IllegalArgumentException("La fecha del evento debe ser futura");
            }

            // REGLA 5: La capacidad debe ser positiva
            if (event.getCapacity() != null && event.getCapacity() <= 0) {
                log.error("EVENT_CREATE_FAILED eventName={} capacity={} reason=InvalidCapacity",
                        event.getName(), event.getCapacity());
                throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
            }

            // REGLA 6: El precio no puede ser negativo
            if (event.getTicketPrice() != null && event.getTicketPrice() < 0) {
                log.error("EVENT_CREATE_FAILED eventName={} price={} reason=NegativePrice",
                        event.getName(), event.getTicketPrice());
                throw new IllegalArgumentException("El precio no puede ser negativo");
            }

            // Si todo está bien, guardar el evento
            Event saved = eventRepository.save(event);

            // Incrementar contador de eventos creados
            eventsCreatedCounter.increment();

            log.info("EVENT_CREATE_SUCCESS eventId={} eventName={} venueId={}",
                    saved.getId(), saved.getName(), saved.getVenueId());

            return saved;
        });
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