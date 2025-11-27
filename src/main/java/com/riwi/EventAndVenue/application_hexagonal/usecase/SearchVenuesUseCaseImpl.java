package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.SearchVenuesUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del caso de uso para buscar venues con filtros dinámicos.
 *
 * Delega la búsqueda al repositorio que implementa las Specifications.
 */
public class SearchVenuesUseCaseImpl implements SearchVenuesUseCase {

    private static final Logger log = LoggerFactory.getLogger(SearchVenuesUseCaseImpl.class);
    private final VenueRepositoryPort venueRepository;

    public SearchVenuesUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Venue> execute(String location, Integer minCapacity, Integer maxCapacity,
                               Boolean active, String name, Boolean hasEvents) {

        log.info("VENUE_SEARCH_START location={} active={} name={}", location, active, name);

        List<Venue> venues = venueRepository.findByFilters(location, minCapacity, maxCapacity,
                active, name, hasEvents);

        log.info("VENUE_SEARCH_SUCCESS count={}", venues.size());

        return venues;
    }
}