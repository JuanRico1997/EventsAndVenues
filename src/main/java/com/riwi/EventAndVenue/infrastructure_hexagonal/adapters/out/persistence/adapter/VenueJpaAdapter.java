package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.adapter;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper.VenueJpaMapper;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.VenueJpaRepository;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.specification.VenueSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA que implementa el puerto de salida VenueRepositoryPort.
 *
 * Este adaptador conecta el dominio con la infraestructura de persistencia JPA.
 * Usa MapStruct para convertir entre Venue (dominio) y VenueEntity (JPA).
 */
@Component
public class VenueJpaAdapter implements VenueRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(VenueJpaAdapter.class);
    private final VenueJpaRepository jpaRepository;
    private final VenueJpaMapper mapper;

    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente el repositorio y el mapper.
     */
    public VenueJpaAdapter(VenueJpaRepository jpaRepository, VenueJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Venue save(Venue venue) {
        log.debug("DB_SAVE_VENUE venueName={}", venue.getName());

        // Convertir de dominio a entidad JPA
        VenueEntity entity = mapper.toEntity(venue);

        // Guardar en la base de datos
        VenueEntity savedEntity = jpaRepository.save(entity);

        // Convertir de entidad JPA a dominio
        Venue result = mapper.toDomain(savedEntity);

        log.debug("DB_SAVE_VENUE_SUCCESS venueId={} venueName={}", result.getId(), result.getName());

        return result;
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain); // Convertir a dominio si existe
    }

    @Override
    public List<Venue> findAll() {
        List<VenueEntity> entities = jpaRepository.findAll();
        return mapper.toDomainList(entities); // Convertir lista completa
    }

    @Override
    public void deleteById(Long id) {
        log.debug("DB_DELETE_VENUE venueId={}", id);

        jpaRepository.deleteById(id);

        log.debug("DB_DELETE_VENUE_SUCCESS venueId={}", id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Venue> findByActive(Boolean active) {
        List<VenueEntity> entities = jpaRepository.findByActive(active);
        return mapper.toDomainList(entities);
    }

    @Override
    public List<Venue> findByLocationIgnoreCase(String location) {
        List<VenueEntity> entities = jpaRepository.findByLocationIgnoreCase(location);
        return mapper.toDomainList(entities);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public List<Venue> findByCapacityGreaterThanEqual(Integer capacity) {
        List<VenueEntity> entities = jpaRepository.findByCapacityGreaterThanEqual(capacity);
        return mapper.toDomainList(entities);
    }

    /**
     * Busca venues aplicando filtros dinámicos con Specifications.
     *
     * @param location Filtro por ubicación parcial (opcional)
     * @param minCapacity Filtro por capacidad mínima (opcional)
     * @param maxCapacity Filtro por capacidad máxima (opcional)
     * @param active Filtro por estado activo/inactivo (opcional)
     * @param name Filtro por nombre parcial (opcional)
     * @param hasEvents Filtro por venues con/sin eventos (opcional)
     * @return Lista de venues que cumplen los filtros
     */
    public List<Venue> findByFilters(String location, Integer minCapacity, Integer maxCapacity,
                                     Boolean active, String name, Boolean hasEvents) {
        // Construir Specification combinando los filtros
        Specification<VenueEntity> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();

        if (location != null && !location.trim().isEmpty()) {
            spec = spec.and(VenueSpecifications.hasLocation(location));
        }
        if (minCapacity != null) {
            spec = spec.and(VenueSpecifications.hasMinCapacity(minCapacity));
        }
        if (maxCapacity != null) {
            spec = spec.and(VenueSpecifications.hasMaxCapacity(maxCapacity));
        }
        if (active != null) {
            spec = spec.and(VenueSpecifications.isActive(active));
        }
        if (name != null && !name.trim().isEmpty()) {
            spec = spec.and(VenueSpecifications.nameContains(name));
        }
        if (hasEvents != null) {
            if (hasEvents) {
                spec = spec.and(VenueSpecifications.hasEvents());
            } else {
                spec = spec.and(VenueSpecifications.hasNoEvents());
            }
        }

        // Ejecutar la búsqueda con las Specifications
        List<VenueEntity> entities = jpaRepository.findAll(spec);

        // Convertir a dominio
        return mapper.toDomainList(entities);
    }
}