package com.riwi.h1.application.service;

import com.riwi.h1.domain.entity.Event;
import com.riwi.h1.domain.entity.Venue;
import com.riwi.h1.domain.repository.EventRepository;
import com.riwi.h1.domain.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class VenueService {

    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;


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

        return venueRepository.save(venue);
    }


    public List<Venue> findAll() {
        return venueRepository.findAll();
    }


    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id);
    }

    public Venue update(Long id, Venue venueData) {
        // Verificar que el venue existe
        Venue existingVenue = venueRepository.findById(id)
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

        return venueRepository.update(existingVenue);
    }


    public boolean deleteById(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new IllegalArgumentException("Venue with ID " + id + " not found");
        }

        // Validar que no tenga eventos asociados
        List<Event> associatedEvents = eventRepository.findByVenueId(id);
        if (!associatedEvents.isEmpty()) {
            throw new IllegalArgumentException(
                    "Cannot delete venue with ID " + id +
                            " because it has " + associatedEvents.size() + " associated event(s)"
            );
        }

        return venueRepository.deleteById(id);
    }


    public List<Venue> findByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        return venueRepository.findByCity(city);
    }


    public List<Venue> findByAvailable(Boolean available) {
        if (available == null) {
            throw new IllegalArgumentException("Available status cannot be null");
        }
        return venueRepository.findByAvailable(available);
    }


    public List<Venue> findAvailableVenues() {
        return venueRepository.findByAvailable(true);
    }

    public long countEventsByVenue(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Venue with ID " + venueId + " not found");
        }
        return eventRepository.findByVenueId(venueId).size();
    }

    public Venue markAsUnavailable(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + venueId + " not found"));

        venue.setAvailable(false);
        return venueRepository.update(venue);
    }

    public Venue markAsAvailable(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new IllegalArgumentException("Venue with ID " + venueId + " not found"));

        venue.setAvailable(true);
        return venueRepository.update(venue);
    }

    // ========== MÉTODOS DE VALIDACIÓN PRIVADOS ==========


    private void validateVenueName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Venue name cannot be empty");
        }
    }
}