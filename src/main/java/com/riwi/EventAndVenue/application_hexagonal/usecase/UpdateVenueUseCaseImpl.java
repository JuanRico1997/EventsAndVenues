package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.UpdateVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;

/**
 * Implementación del caso de uso: Actualizar Venue.
 *
 * Contiene toda la lógica de negocio para actualizar un venue existente.
 */
public class UpdateVenueUseCaseImpl implements UpdateVenueUseCase {

    private final VenueRepositoryPort venueRepository;

    public UpdateVenueUseCaseImpl(VenueRepositoryPort venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    public Venue execute(Long id, Venue venueData) {
        // REGLA 1: Verificar que el venue existe
        Venue existingVenue = venueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Venue con ID " + id + " no encontrado"
                ));

        // REGLA 2: Actualizar nombre si se proporciona y validar
        if (venueData.getName() != null) {
            validateVenueName(venueData.getName());

            // Verificar duplicados solo si el nombre cambió
            if (!venueData.getName().equalsIgnoreCase(existingVenue.getName())) {
                if (venueRepository.existsByNameIgnoreCase(venueData.getName())) {
                    throw new IllegalArgumentException(
                            "Ya existe un venue con el nombre: " + venueData.getName()
                    );
                }
            }

            existingVenue.setName(venueData.getName());
        }

        // REGLA 3: Actualizar ubicación si se proporciona y validar
        if (venueData.getLocation() != null) {
            validateVenueLocation(venueData.getLocation());
            existingVenue.setLocation(venueData.getLocation());
        }

        // REGLA 4: Actualizar capacidad si se proporciona y validar
        if (venueData.getCapacity() != null) {
            if (venueData.getCapacity() <= 0) {
                throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
            }
            existingVenue.setCapacity(venueData.getCapacity());
        }

        // REGLA 5: Actualizar descripción si se proporciona
        if (venueData.getDescription() != null) {
            existingVenue.setDescription(venueData.getDescription());
        }

        // REGLA 6: Actualizar estado activo si se proporciona
        if (venueData.getActive() != null) {
            existingVenue.setActive(venueData.getActive());
        }

        // Guardar los cambios
        return venueRepository.save(existingVenue);
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