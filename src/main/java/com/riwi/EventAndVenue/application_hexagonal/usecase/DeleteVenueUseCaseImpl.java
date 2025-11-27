package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del caso de uso: Eliminar Venue.
 *
 * Contiene la lógica de negocio para eliminar un venue.
 */
public class DeleteVenueUseCaseImpl implements DeleteVenueUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteVenueUseCaseImpl.class);
    private final VenueRepositoryPort venueRepository;
    private final EventRepositoryPort eventRepository;

    public DeleteVenueUseCaseImpl(VenueRepositoryPort venueRepository,
                                  EventRepositoryPort eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public void execute(Long id) {
        log.info("VENUE_DELETE_START venueId={}", id);

        // Verificar que existe antes de eliminar
        if (!venueRepository.existsById(id)) {
            log.error("VENUE_DELETE_FAILED venueId={} reason=VenueNotFound", id);
            throw new EntityNotFoundException("Venue not found with id: " + id);
        }

        venueRepository.deleteById(id);

        log.info("VENUE_DELETE_SUCCESS venueId={}", id);
    }
}