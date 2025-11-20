package com.riwi.h1.application.service;

import com.riwi.h1.domain.entity.Event;
import com.riwi.h1.domain.entity.Venue;
import com.riwi.h1.domain.repository.jpa.EventJpaRepository;
import com.riwi.h1.domain.repository.jpa.VenueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Venues (lugares/recintos).
 *
 * MIGRADO A JPA:
 * - Ahora usa VenueJpaRepository (JPA) en lugar de VenueRepositoryImpl (in-memory)
 * - Utiliza EventJpaRepository (JPA) en lugar de EventRepositoryImpl (in-memory)
 * - Los datos se persisten en la base de datos H2
 * - Mantiene toda la lógica de validación de negocio
 */
@Service
@RequiredArgsConstructor
public class VenueService {

    // CAMBIO: Ahora inyectamos los repositorios JPA
    private final VenueJpaRepository venueJpaRepository;
    private final EventJpaRepository eventJpaRepository;


    public Venue create(Venue venue) {
        // Validación: nombre no puede estar vacío
        validateVenueName(venue.getName());

        // Validación: capacidad máxima debe ser positiva
        if (venue.getMaxCapacity() != null && venue.getMaxCapacity() <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than 0");
        }

        // Validación: dirección no puede estar vacía
        if (venue.getAddress() == null || venue.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }

        // Validación: ciudad no puede estar vacía
        if (venue.getCity() == null || venue.getCity().trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }

        // CAMBIO: Usa save() de JPA
        return venueJpaRepository.save(venue);
    }


    public List<Venue> findAll() {
        return venueJpaRepository.findAll();
    }


    public Optional<Venue> findById(Long id) {
        return venueJpaRepository.findById(id);
    }

    public Venue update(Long id, Venue venueData) {
        // Verificar que el venue existe
        Venue existingVenue = venueJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + id + " not found"));

        // Validar y actualizar nombre
        if (venueData.getName() != null) {
            validateVenueName(venueData.getName());
            existingVenue.setName(venueData.getName());
        }

        // Validar y actualizar dirección
        if (venueData.getAddress() != null) {
            if (venueData.getAddress().trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be empty");
            }
            existingVenue.setAddress(venueData.getAddress());
        }

        // Validar y actualizar ciudad
        if (venueData.getCity() != null) {
            if (venueData.getCity().trim().isEmpty()) {
                throw new IllegalArgumentException("City cannot be empty");
            }
            existingVenue.setCity(venueData.getCity());
        }

        // Actualizar país
        if (venueData.getCountry() != null) {
            existingVenue.setCountry(venueData.getCountry());
        }

        // Validar y actualizar capacidad máxima
        if (venueData.getMaxCapacity() != null) {
            if (venueData.getMaxCapacity() <= 0) {
                throw new IllegalArgumentException("Max capacity must be greater than 0");
            }
            existingVenue.setMaxCapacity(venueData.getMaxCapacity());
        }

        // Actualizar tipo
        if (venueData.getType() != null) {
            existingVenue.setType(venueData.getType());
        }

        // Actualizar disponibilidad
        if (venueData.getAvailable() != null) {
            existingVenue.setAvailable(venueData.getAvailable());
        }

        // CAMBIO: En JPA, save() sirve tanto para crear como actualizar
        return venueJpaRepository.save(existingVenue);
    }


    public boolean deleteById(Long id) {
        if (!venueJpaRepository.existsById(id)) {
            throw new IllegalArgumentException("Venue with ID " + id + " not found");
        }

        // Validar que no tenga eventos asociados
        List<Event> associatedEvents = eventJpaRepository.findByVenueId(id);
        if (!associatedEvents.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot delete venue with ID " + id +
                            " because it has " + associatedEvents.size() + " associated event(s)"
            );
        }

        // CAMBIO: JPA usa deleteById() que no retorna boolean
        venueJpaRepository.deleteById(id);
        return true; // Si no lanza excepción, se eliminó correctamente
    }


    public List<Venue> findByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        return venueJpaRepository.findByCity(city);
    }


    public List<Venue> findByAvailable(Boolean available) {
        if (available == null) {
            throw new IllegalArgumentException("Available status cannot be null");
        }
        return venueJpaRepository.findByAvailable(available);
    }


    public List<Venue> findAvailableVenues() {
        return venueJpaRepository.findByAvailable(true);
    }

    public long countEventsByVenue(Long venueId) {
        if (!venueJpaRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Venue with ID " + venueId + " not found");
        }
        // MEJORA: Usamos el método count de JPA que es más eficiente
        return eventJpaRepository.countByVenueId(venueId);
    }

    public Venue markAsUnavailable(Long venueId) {
        Venue venue = venueJpaRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + venueId + " not found"));

        venue.setAvailable(false);
        return venueJpaRepository.save(venue);
    }

    public Venue markAsAvailable(Long venueId) {
        Venue venue = venueJpaRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + venueId + " not found"));

        venue.setAvailable(true);
        return venueJpaRepository.save(venue);
    }

    // ========== MÉTODOS DE VALIDACIÓN PRIVADOS ==========


    private void validateVenueName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Venue name cannot be empty");
        }
    }
}