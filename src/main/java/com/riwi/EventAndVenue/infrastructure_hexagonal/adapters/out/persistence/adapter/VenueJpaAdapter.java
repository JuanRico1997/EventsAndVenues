package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.adapter;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper.VenueJpaMapper;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.VenueJpaRepository;
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
        // Convertir de dominio a entidad JPA
        VenueEntity entity = mapper.toEntity(venue);

        // Guardar en la base de datos
        VenueEntity savedEntity = jpaRepository.save(entity);

        // Convertir de entidad JPA a dominio
        return mapper.toDomain(savedEntity);
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
        jpaRepository.deleteById(id);
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
}