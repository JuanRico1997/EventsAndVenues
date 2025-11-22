package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;

/**
 * Implementación del caso de uso: Eliminar Venue.
 *
 * Contiene la lógica de negocio para eliminar un venue.
 */
public class DeleteVenueUseCaseImpl implements DeleteVenueUseCase {

    private final VenueRepositoryPort venueRepository;
    private final EventRepositoryPort eventRepository;

    public DeleteVenueUseCaseImpl(VenueRepositoryPort venueRepository,
                                  EventRepositoryPort eventRepository) {
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void execute(Long id) {
        // REGLA 1: Verificar que el venue existe
        if (!venueRepository.existsById(id)) {
            throw new IllegalArgumentException("Venue con ID " + id + " no encontrado");
        }

        // REGLA 2 (OPCIONAL): Verificar que no tenga eventos asociados
        long eventCount = eventRepository.countByVenueId(id);
        if (eventCount > 0) {
            throw new IllegalArgumentException(
                    "No se puede eliminar el venue porque tiene " + eventCount + " evento(s) asociado(s)"
            );
        }

        // Eliminar el venue
        venueRepository.deleteById(id);
    }
}