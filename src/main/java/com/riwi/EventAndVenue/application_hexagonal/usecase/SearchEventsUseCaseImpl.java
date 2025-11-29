package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.SearchEventsUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del caso de uso para buscar eventos con filtros dinámicos.
 *
 * Delega la búsqueda al repositorio que implementa las Specifications.
 */
public class SearchEventsUseCaseImpl implements SearchEventsUseCase {

    private static final Logger log = LoggerFactory.getLogger(SearchEventsUseCaseImpl.class);
    private final EventRepositoryPort eventRepository;

    public SearchEventsUseCaseImpl(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> execute(Long venueId, Boolean active, LocalDateTime startDate,
                               LocalDateTime endDate, String name, Integer minCapacity,
                               Double maxPrice) {

        log.info("EVENT_SEARCH_START venueId={} active={} name={}", venueId, active, name);

        List<Event> events = eventRepository.findByFilters(venueId, active, startDate, endDate,
                name, minCapacity, maxPrice);

        log.info("EVENT_SEARCH_SUCCESS count={}", events.size());

        return events;
    }
}