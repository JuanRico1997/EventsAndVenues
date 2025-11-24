package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.adapter;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.EventEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper.EventJpaMapper;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.EventJpaRepository;
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
        // Convertir de dominio a entidad JPA
        EventEntity entity = mapper.toEntity(event);

        // Guardar en la base de datos
        EventEntity savedEntity = jpaRepository.save(entity);

        // Convertir de entidad JPA a dominio
        return mapper.toDomain(savedEntity);
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
        jpaRepository.deleteById(id);
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
}