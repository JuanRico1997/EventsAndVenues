package com.riwi.EventAndVenue.application_hexagonal.service;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de consultas para Event.
 *
 * Maneja consultas simples SIN lógica de negocio.
 * Para operaciones con lógica, usar los casos de uso.
 */
public class EventQueryService {

    private final EventRepositoryPort eventRepository;

    public EventQueryService(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Busca un evento por su ID.
     */
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    /**
     * Obtiene todos los eventos.
     */
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    /**
     * Busca eventos por venue.
     */
    public List<Event> findByVenueId(Long venueId) {
        return eventRepository.findByVenueId(venueId);
    }

    /**
     * Busca eventos activos.
     */
    public List<Event> findActiveEvents() {
        return eventRepository.findByActive(true);
    }

    /**
     * Busca eventos próximos (futuros).
     */
    public List<Event> findUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findByEventDateAfter(now);
    }

    /**
     * Busca eventos en un rango de fechas.
     */
    public List<Event> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByEventDateBetween(startDate, endDate);
    }

    /**
     * Cuenta eventos por venue.
     */
    public long countByVenueId(Long venueId) {
        return eventRepository.countByVenueId(venueId);
    }
}