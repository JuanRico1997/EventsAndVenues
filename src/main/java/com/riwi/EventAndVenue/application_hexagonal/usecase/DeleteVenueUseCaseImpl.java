package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementación del caso de uso: Eliminar Venue.
 *
 * Contiene la lógica de negocio para eliminar un venue.
 */
public class DeleteVenueUseCaseImpl implements DeleteVenueUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteVenueUseCaseImpl.class);
    private final VenueRepositoryPort venueRepository;
    private final EventRepositoryPort eventRepository;
    private final Counter venuesDeletedCounter;

    public DeleteVenueUseCaseImpl(VenueRepositoryPort venueRepository,
                                  EventRepositoryPort eventRepository,
                                  @Qualifier("venuesDeletedCounter") Counter venuesDeletedCounter) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.venuesDeletedCounter = venuesDeletedCounter;
    }

    @Transactional
    @Override
    public void execute(Long venueId) {
        log.info("VENUE_DELETE_START venueId={}", venueId);

        // Verificar que el venue existe antes de eliminar
        if (!venueRepository.existsById(venueId)) {
            log.error("VENUE_DELETE_FAILED venueId={} reason=VenueNotFound", venueId);
            throw new EntityNotFoundException("Venue not found with id: " + venueId);
        }

        // Eliminar el venue
        venueRepository.deleteById(venueId);

        // Incrementar contador de venues eliminados
        venuesDeletedCounter.increment();

        log.info("VENUE_DELETE_SUCCESS venueId={}", venueId);
    }
}