package com.riwi.h1.application.service;


import com.riwi.h1.domain.entity.Event;
import com.riwi.h1.domain.repository.EventRepository;
import com.riwi.h1.domain.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public Event create(Event event) {

        //Validacion nombre not null
        validateEventName(event.getName());

        // Validación: si tiene venueId, debe existir el venue
        if (event.getVenueId() != null) {
            validateVenueExists(event.getVenueId());
        }

        // Validación: la fecha del evento debe ser futura
        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }

        // Validación: capacidad debe ser positiva
        if (event.getCapacity() != null && event.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        // Validación: precio debe ser positivo
        if (event.getTicketPrice() != null && event.getTicketPrice() < 0) {
            throw new IllegalArgumentException("Ticket price cannot be negative");
        }

        return eventRepository.save(event);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event update(Long id, Event eventData) {
        // Verificar que el evento existe
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with ID " + id + " not found"));

        // Validar nombre si se proporciona
        if (eventData.getName() != null) {
            validateEventName(eventData.getName());
            existingEvent.setName(eventData.getName());
        }

        // Validar y actualizar descripción
        if (eventData.getDescription() != null) {
            existingEvent.setDescription(eventData.getDescription());
        }

        // Validar y actualizar fecha del evento
        if (eventData.getEventDate() != null) {
            if (eventData.getEventDate().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Event date must be in the future");
            }
            existingEvent.setEventDate(eventData.getEventDate());
        }

        // Validar y actualizar venue
        if (eventData.getVenueId() != null) {
            validateVenueExists(eventData.getVenueId());
            existingEvent.setVenueId(eventData.getVenueId());
        }

        // Validar y actualizar capacidad
        if (eventData.getCapacity() != null) {
            if (eventData.getCapacity() <= 0) {
                throw new IllegalArgumentException("Capacity must be greater than 0");
            }
            existingEvent.setCapacity(eventData.getCapacity());
        }

        // Validar y actualizar precio
        if (eventData.getTicketPrice() != null) {
            if (eventData.getTicketPrice() < 0) {
                throw new IllegalArgumentException("Ticket price cannot be negative");
            }
            existingEvent.setTicketPrice(eventData.getTicketPrice());
        }

        // Actualizar estado activo
        if (eventData.getActive() != null) {
            existingEvent.setActive(eventData.getActive());
        }

        return eventRepository.update(existingEvent);
    }

    public boolean deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Event with ID " + id + " not found");
        }
        return eventRepository.deleteById(id);
    }

    public List<Event> findByVenueId(Long venueId) {
        // Validar que el venue existe
        validateVenueExists(venueId);
        return eventRepository.findByVenueId(venueId);
    }


    public List<Event> findActiveEvents() {
        return eventRepository.findAll().stream()
                .filter(Event::getActive)
                .toList();
    }


    public List<Event> findUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        return eventRepository.findAll().stream()
                .filter(event -> event.getEventDate() != null && event.getEventDate().isAfter(now))
                .toList();
    }

    // ========== MÉTODOS DE VALIDACIÓN PRIVADOS ==========


    private void validateEventName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
    }


    private void validateVenueExists(Long venueId) {
        if (!venueRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Venue with ID " + venueId + " not found");
        }
    }
}
