package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.CreateVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementación del caso de uso: Crear Venue.
 *
 * Contiene toda la lógica de negocio para crear un nuevo venue.
 */
public class CreateVenueUseCaseImpl implements CreateVenueUseCase {

    private final VenueRepositoryPort venueRepository;
    private static final Logger log = LoggerFactory.getLogger(CreateVenueUseCaseImpl.class);
    public CreateVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }
    private Counter venuesCreatedCounter;
    private Timer venueCreationTimer;

    public CreateVenueUseCaseImpl(
            VenueRepositoryPort venueRepository,
            @Qualifier("venuesCreatedCounter") Counter venuesCreatedCounter,
            @Qualifier("venueCreationTimer") Timer venueCreationTimer) {
        this.venueRepository = venueRepository;
        this.venuesCreatedCounter = venuesCreatedCounter;
        this.venueCreationTimer = venueCreationTimer;
    }

    @Transactional
    @Override
    public Venue execute(Venue venue) {
        return venueCreationTimer.record(() -> {
            log.info("VENUE_CREATE_START venueName={}", venue.getName());

            // REGLA 1: El nombre no puede estar vacío
            validateVenueName(venue.getName());

            // REGLA 2: No puede existir otro venue con el mismo nombre
            if (venueRepository.existsByNameIgnoreCase(venue.getName())) {
                log.error("VENUE_CREATE_FAILED venueName={} reason=DuplicateName", venue.getName());
                throw new IllegalArgumentException(
                        "Ya existe un venue con el nombre: " + venue.getName()
                );
            }

            // REGLA 3: La ubicación no puede estar vacía
            if (venue.getLocation() == null || venue.getLocation().trim().isEmpty()) {
                log.error("VENUE_CREATE_FAILED venueName={} reason=EmptyLocation", venue.getName());
                throw new IllegalArgumentException("La ubicación del venue no puede estar vacía");
            }

            // REGLA 4: La capacidad debe ser positiva
            if (venue.getCapacity() != null && venue.getCapacity() <= 0) {
                log.error("VENUE_CREATE_FAILED venueName={} capacity={} reason=InvalidCapacity",
                        venue.getName(), venue.getCapacity());
                throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
            }

            // Si todo está bien, guardar el venue
            Venue saved = venueRepository.save(venue);

            // Incrementar contador de venues creados
            venuesCreatedCounter.increment();

            log.info("VENUE_CREATE_SUCCESS venueId={} venueName={}",
                    saved.getId(), saved.getName());

            return saved;
        });
    }

    // ========== MÉTODOS PRIVADOS DE VALIDACIÓN ==========

    private void validateVenueName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del venue no puede estar vacío");
        }
    }

    private void validateVenueLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicación del venue no puede estar vacía");
        }
    }
}