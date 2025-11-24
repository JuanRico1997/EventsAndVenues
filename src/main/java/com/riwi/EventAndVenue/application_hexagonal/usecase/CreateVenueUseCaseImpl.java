package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.CreateVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;

/**
 * Implementación del caso de uso: Crear Venue.
 *
 * Contiene toda la lógica de negocio para crear un nuevo venue.
 */
public class CreateVenueUseCaseImpl implements CreateVenueUseCase {

    private final VenueRepositoryPort venueRepository;

    public CreateVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue execute(Venue venue) {
        // REGLA 1: El nombre no puede estar vacío
        validateVenueName(venue.getName());

        // REGLA 2: No puede existir otro venue con el mismo nombre
        if (venueRepository.existsByNameIgnoreCase(venue.getName())) {
            throw new IllegalArgumentException(
                    "Ya existe un venue con el nombre: " + venue.getName()
            );
        }

        // REGLA 3: La ubicación no puede estar vacía
        validateVenueLocation(venue.getLocation());

        // REGLA 4: La capacidad debe ser positiva
        if (venue.getCapacity() != null && venue.getCapacity() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }

        // Si todo está bien, guardar el venue
        return venueRepository.save(venue);
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