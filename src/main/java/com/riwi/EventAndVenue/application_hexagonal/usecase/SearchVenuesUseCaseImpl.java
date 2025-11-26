package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.SearchVenuesUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;

import java.util.List;

/**
 * Implementación del caso de uso para buscar venues con filtros dinámicos.
 *
 * Delega la búsqueda al repositorio que implementa las Specifications.
 */
public class SearchVenuesUseCaseImpl implements SearchVenuesUseCase {

    private final VenueRepositoryPort venueRepository;

    public SearchVenuesUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public List<Venue> execute(String location, Integer minCapacity, Integer maxCapacity,
                               Boolean active, String name, Boolean hasEvents) {
        // Delegar al repositorio
        return venueRepository.findByFilters(location, minCapacity, maxCapacity,
                active, name, hasEvents);
    }
}