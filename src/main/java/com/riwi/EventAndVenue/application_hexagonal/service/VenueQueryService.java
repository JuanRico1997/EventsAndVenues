package com.riwi.EventAndVenue.application_hexagonal.service;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de consultas para Venue.
 *
 * Maneja consultas simples SIN lógica de negocio.
 * Para operaciones con lógica, usar los casos de uso.
 */
public class VenueQueryService {

    private final VenueRepositoryPort venueRepository;

    public VenueQueryService(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    /**
     * Busca un venue por su ID.
     */
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id);
    }

    /**
     * Obtiene todos los venues.
     */
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    /**
     * Busca venues activos.
     */
    public List<Venue> findActiveVenues() {
        return venueRepository.findByActive(true);
    }

    /**
     * Busca venues por ubicación.
     */
    public List<Venue> findByLocation(String location) {
        return venueRepository.findByLocationIgnoreCase(location);
    }

    /**
     * Busca venues con capacidad mínima.
     */
    public List<Venue> findByMinimumCapacity(Integer capacity) {
        return venueRepository.findByCapacityGreaterThanEqual(capacity);
    }
}