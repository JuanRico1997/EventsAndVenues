package com.riwi.EventAndVenue.application.service;

import com.riwi.EventAndVenue.domain_old.entity.Event;
import com.riwi.EventAndVenue.domain_old.entity.Venue;
import com.riwi.EventAndVenue.domain_old.repository.jpa.EventJpaRepository;
import com.riwi.EventAndVenue.domain_old.repository.jpa.VenueJpaRepository;
import com.riwi.EventAndVenue.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        // ========== 🆕 NUEVA VALIDACIÓN: Verificar duplicados ==========
        // Verifica si ya existe otro venue con el mismo nombre (ignora mayúsculas)
        if (venueJpaRepository.existsByNameIgnoreCase(venue.getName())) {
            throw new DuplicateResourceException("Venue", "name", venue.getName());
        }

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

            // ========== 🆕 NUEVA VALIDACIÓN: Verificar duplicados al actualizar ==========
            // Solo valida duplicados si el nombre cambió
            if (!venueData.getName().equalsIgnoreCase(existingVenue.getName())) {
                if (venueJpaRepository.existsByNameIgnoreCase(venueData.getName())) {
                    throw new DuplicateResourceException("Venue", "name", venueData.getName());
                }
            }

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
        // MEJORA: Usamos el metodo count de JPA que es más eficiente
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

    // ========== 🆕 NUEVOS MÉTODOS CON PAGINACIÓN ==========

    /**
     * Busca todos los venues con paginación.
     *
     * @param pageable Configuración de paginación
     * @return Página de venues
     */
    public Page<Venue> findAllPaginated(Pageable pageable) {
        return venueJpaRepository.findAll(pageable);
    }

    /**
     * Busca venues por ciudad con paginación.
     *
     * @param city Ciudad
     * @param pageable Configuración de paginación
     * @return Página de venues en la ciudad
     */
    public Page<Venue> findByCityPaginated(String city, Pageable pageable) {
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be empty");
        }
        return venueJpaRepository.findByCity(city, pageable);
    }

    /**
     * Busca venues por tipo con paginación.
     *
     * @param type Tipo de venue
     * @param pageable Configuración de paginación
     * @return Página de venues del tipo especificado
     */
    public Page<Venue> findByTypePaginated(String type, Pageable pageable) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Type cannot be empty");
        }
        return venueJpaRepository.findByType(type, pageable);
    }

    /**
     * Busca venues disponibles con paginación.
     *
     * @param available Estado de disponibilidad
     * @param pageable Configuración de paginación
     * @return Página de venues disponibles/no disponibles
     */
    public Page<Venue> findByAvailablePaginated(Boolean available, Pageable pageable) {
        if (available == null) {
            throw new IllegalArgumentException("Available status cannot be null");
        }
        return venueJpaRepository.findByAvailable(available, pageable);
    }

    /**
     * Busca venues con capacidad mínima con paginación.
     *
     * @param minCapacity Capacidad mínima requerida
     * @param pageable Configuración de paginación
     * @return Página de venues que cumplen la capacidad
     */
    public Page<Venue> findByMinCapacityPaginated(Integer minCapacity, Pageable pageable) {
        if (minCapacity == null || minCapacity <= 0) {
            throw new IllegalArgumentException("Min capacity must be positive");
        }
        return venueJpaRepository.findByMaxCapacityGreaterThanEqual(minCapacity, pageable);
    }

    /**
     * Busca venues con filtros opcionales y paginación.
     *
     * @param city Ciudad (opcional)
     * @param country País (opcional)
     * @param type Tipo (opcional)
     * @param available Disponibilidad (opcional)
     * @param minCapacity Capacidad mínima (opcional)
     * @param pageable Configuración de paginación
     * @return Página de venues filtrados
     */
    public Page<Venue> findWithFilters(String city, String country, String type,
                                       Boolean available, Integer minCapacity,
                                       Pageable pageable) {
        return venueJpaRepository.findWithFilters(city, country, type, available, minCapacity, pageable);
    }

    // ========== MÉTODOS DE VALIDACIÓN PRIVADOS ==========


    private void validateVenueName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Venue name cannot be empty");
        }
    }
}