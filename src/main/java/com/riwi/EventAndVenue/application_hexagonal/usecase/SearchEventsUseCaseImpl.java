package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.SearchEventsUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del caso de uso para buscar eventos con filtros dinámicos.
 *
 * Delega la búsqueda al repositorio que implementa las Specifications.
 */
public class SearchEventsUseCaseImpl implements SearchEventsUseCase {

    private final EventRepositoryPort eventRepository;

    public SearchEventsUseCaseImpl(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> execute(Long venueId, Boolean active, LocalDateTime startDate,
                               LocalDateTime endDate, String name, Integer minCapacity,
                               Double maxPrice) {
        // Delegar al repositorio
        return eventRepository.findByFilters(venueId, active, startDate, endDate,
                name, minCapacity, maxPrice);
    }
}