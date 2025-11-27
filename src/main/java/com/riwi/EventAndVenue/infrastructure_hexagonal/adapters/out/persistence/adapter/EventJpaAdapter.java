package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.adapter;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.EventEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper.EventJpaMapper;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.EventJpaRepository;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.specification.EventSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA que implementa el puerto de salida EventRepositoryPort.
 *
 * Este adaptador conecta el dominio con la infraestructura de persistencia JPA.
 * Usa MapStruct para convertir entre Event (dominio) y EventEntity (JPA).
 */
@Component
public class EventJpaAdapter implements EventRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(EventJpaAdapter.class);
    private final EventJpaRepository jpaRepository;
    private final EventJpaMapper mapper;

    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente el repositorio y el mapper.
     */
    public EventJpaAdapter(EventJpaRepository jpaRepository, EventJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Event save(Event event) {
        log.debug("DB_SAVE_EVENT eventName={}", event.getName());

        // Convertir de dominio a entidad JPA
        EventEntity entity = mapper.toEntity(event);

        // Guardar en la base de datos
        EventEntity savedEntity = jpaRepository.save(entity);

        // Convertir de entidad JPA a dominio
        Event result = mapper.toDomain(savedEntity);

        log.debug("DB_SAVE_EVENT_SUCCESS eventId={} eventName={}", result.getId(), result.getName());

        return result;
    }

    @Override
    public Optional<Event> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain); // Convertir a dominio si existe
    }

    @Override
    public List<Event> findAll() {
        List<EventEntity> entities = jpaRepository.findAll();
        return mapper.toDomainList(entities); // Convertir lista completa
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DB_DELETE_EVENT eventId={}", id);

        jpaRepository.deleteById(id);

        log.debug("DB_DELETE_EVENT_SUCCESS eventId={}", id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Event> findByVenueId(Long venueId) {
        List<EventEntity> entities = jpaRepository.findByVenueId(venueId);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Event> findByActive(Boolean active) {
        List<EventEntity> entities = jpaRepository.findByActive(active);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Event> findByEventDateAfter(LocalDateTime date) {
        List<EventEntity> entities = jpaRepository.findByEventDateAfter(date);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Event> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<EventEntity> entities = jpaRepository.findByEventDateBetween(startDate, endDate);
        return mapper.toDomainList(entities);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public long countByVenueId(Long venueId) {
        return jpaRepository.countByVenueId(venueId);
    }

    /**
     * Busca eventos aplicando filtros dinámicos con Specifications.
     *
     * @param venueId Filtro por venue (opcional)
     * @param active Filtro por estado activo/inactivo (opcional)
     * @param startDate Filtro por fecha inicio (opcional)
     * @param endDate Filtro por fecha fin (opcional)
     * @param name Filtro por nombre parcial (opcional)
     * @param minCapacity Filtro por capacidad mínima (opcional)
     * @param maxPrice Filtro por precio máximo (opcional)
     * @return Lista de eventos que cumplen los filtros
     */
    public List<Event> findByFilters(Long venueId, Boolean active, LocalDateTime startDate,
                                     LocalDateTime endDate, String name, Integer minCapacity,
                                     Double maxPrice) {
        // Construir Specification combinando los filtros
        Specification<EventEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (venueId != null) {
            spec = spec.and(EventSpecifications.hasVenue(venueId));
        }
        if (active != null) {
            spec = spec.and(EventSpecifications.hasStatus(active));
        }
        if (startDate != null || endDate != null) {
            spec = spec.and(EventSpecifications.isBetweenDates(startDate, endDate));
        }
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(EventSpecifications.nameContains(name));
        }
        if (minCapacity != null) {
            spec = spec.and(EventSpecifications.hasMinCapacity(minCapacity));
        }
        if (maxPrice != null) {
            spec = spec.and(EventSpecifications.hasMaxPrice(maxPrice));
        }

        // Ejecutar la búsqueda con las Specifications
        List<EventEntity> entities = jpaRepository.findAll(spec);

        // Convertir a dominio
        return mapper.toDomainList(entities);
    }
}