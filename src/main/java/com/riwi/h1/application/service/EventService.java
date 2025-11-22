package com.riwi.h1.application.service;


import com.riwi.h1.domain.entity.Event;
import com.riwi.h1.domain.repository.jpa.EventJpaRepository;
import com.riwi.h1.domain.repository.jpa.VenueJpaRepository;
import com.riwi.h1.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Eventos.
 *
 * MIGRADO A JPA:
 * - Ahora usa EventJpaRepository (JPA) en lugar de EventRepositoryImpl (in-memory)
 * - Utiliza VenueJpaRepository (JPA) en lugar de VenueRepositoryImpl (in-memory)
 * - Los datos se persisten en la base de datos H2
 * - Mantiene toda la lógica de validación de negocio
 */
@Service
@RequiredArgsConstructor
public class EventService {

    // CAMBIO: Ahora inyectamos los repositorios JPA
    private final EventJpaRepository eventJpaRepository;
    private final VenueJpaRepository venueJpaRepository;

    public Event create(Event event) {

        //Validacion nombre not null
        validateEventName(event.getName());

        // ========== 🆕 NUEVA VALIDACIÓN: Verificar duplicados ==========
        // Verifica si ya existe otro evento con el mismo nombre (ignora mayúsculas)
        if (eventJpaRepository.existsByNameIgnoreCase(event.getName())) {
            throw new DuplicateResourceException("Event", "name", event.getName());
        }

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

        // CAMBIO: Usa save() de JPA - funciona igual para crear y actualizar
        return eventJpaRepository.save(event);
    }

    public List<Event> findAll() {
        return eventJpaRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventJpaRepository.findById(id);
    }

    public Event update(Long id, Event eventData) {
        // Verificar que el evento existe
        Event existingEvent = eventJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event with ID " + id + " not found"));

        // Validar nombre si se proporciona
        if (eventData.getName() != null) {
            validateEventName(eventData.getName());

            // ========== 🆕 NUEVA VALIDACIÓN: Verificar duplicados al actualizar ==========
            // Solo valida duplicados si el nombre cambió
            if (!eventData.getName().equalsIgnoreCase(existingEvent.getName())) {
                if (eventJpaRepository.existsByNameIgnoreCase(eventData.getName())) {
                    throw new DuplicateResourceException("Event", "name", eventData.getName());
                }
            }

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

        // CAMBIO: En JPA, save() sirve tanto para crear como actualizar
        // Si la entidad tiene ID, hace UPDATE; si no, hace INSERT
        return eventJpaRepository.save(existingEvent);
    }

    public boolean deleteById(Long id) {
        if (!eventJpaRepository.existsById(id)) {
            throw new IllegalArgumentException("Event with ID " + id + " not found");
        }
        // CAMBIO: JPA usa deleteById() que no retorna boolean, pero funciona igual
        eventJpaRepository.deleteById(id);
        return true; // Si no lanza excepción, se eliminó correctamente
    }

    public List<Event> findByVenueId(Long venueId) {
        // Validar que el venue existe
        validateVenueExists(venueId);
        return eventJpaRepository.findByVenueId(venueId);
    }


    public List<Event> findActiveEvents() {
        // MEJORA: Ahora usamos el método de JPA que genera la query automáticamente
        // En lugar de filtrar en memoria con stream(), la BD hace el filtro
        return eventJpaRepository.findByActive(true);
    }


    public List<Event> findUpcomingEvents() {
        LocalDateTime now = LocalDateTime.now();
        // MEJORA: Usamos el método de JPA para filtrar en la BD
        return eventJpaRepository.findByEventDateAfter(now);
    }

    // ========== 🆕 NUEVOS MÉTODOS CON PAGINACIÓN ==========

    /**
     * Busca todos los eventos con paginación.
     *
     * @param pageable Configuración de paginación
     * @return Página de eventos
     */
    public Page<Event> findAllPaginated(Pageable pageable) {
        return eventJpaRepository.findAll(pageable);
    }

    /**
     * Busca eventos activos con paginación.
     *
     * @param pageable Configuración de paginación
     * @return Página de eventos activos
     */
    public Page<Event> findActiveEventsPaginated(Pageable pageable) {
        return eventJpaRepository.findByActive(true, pageable);
    }

    /**
     * Busca eventos futuros con paginación.
     *
     * @param pageable Configuración de paginación
     * @return Página de eventos próximos
     */
    public Page<Event> findUpcomingEventsPaginated(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        return eventJpaRepository.findByEventDateAfter(now, pageable);
    }

    /**
     * Busca eventos por venue con paginación.
     *
     * @param venueId ID del venue
     * @param pageable Configuración de paginación
     * @return Página de eventos del venue
     */
    public Page<Event> findByVenueIdPaginated(Long venueId, Pageable pageable) {
        validateVenueExists(venueId);
        return eventJpaRepository.findByVenueId(venueId, pageable);
    }

    /**
     * Busca eventos con filtros opcionales y paginación.
     *
     * @param venueId ID del venue (opcional)
     * @param active Estado activo (opcional)
     * @param startDate Fecha inicio (opcional)
     * @param pageable Configuración de paginación
     * @return Página de eventos filtrados
     */
    public Page<Event> findWithFilters(Long venueId, Boolean active, LocalDateTime startDate, Pageable pageable) {
        // Validar venue si se proporciona
        if (venueId != null) {
            validateVenueExists(venueId);
        }

        return eventJpaRepository.findWithFilters(venueId, active, startDate, pageable);
    }

    // ========== MÉTODOS DE VALIDACIÓN PRIVADOS ==========


    private void validateEventName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
    }

    private void validateVenueExists(Long venueId) {
        if (!venueJpaRepository.existsById(venueId)) {
            throw new IllegalArgumentException("Venue with ID " + venueId + " not found");
        }
    }
}