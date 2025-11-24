package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper de MapStruct para convertir entre Event (dominio) y EventEntity (JPA).
 *
 * MapStruct genera automáticamente la implementación en tiempo de compilación.
 * Solo definimos las interfaces de los métodos de conversión.
 *
 * componentModel = "spring" - Hace que Spring detecte el mapper como @Component
 */
@Mapper(componentModel = "spring")
public interface EventJpaMapper {

    /**
     * Convierte de EventEntity (JPA) a Event (dominio).
     *
     * @param entity Entidad JPA
     * @return Modelo de dominio
     */
    Event toDomain(EventEntity entity);

    /**
     * Convierte de Event (dominio) a EventEntity (JPA).
     *
     * @param domain Modelo de dominio
     * @return Entidad JPA
     */
    EventEntity toEntity(Event domain);

    /**
     * Convierte una lista de EventEntity a lista de Event.
     *
     * @param entities Lista de entidades JPA
     * @return Lista de modelos de dominio
     */
    List<Event> toDomainList(List<EventEntity> entities);

    /**
     * Convierte una lista de Event a lista de EventEntity.
     *
     * @param domains Lista de modelos de dominio
     * @return Lista de entidades JPA
     */
    List<EventEntity> toEntityList(List<Event> domains);

    /**
     * Actualiza una entidad existente con datos del dominio.
     * Útil para operaciones de UPDATE sin perder el ID.
     *
     * @param domain Modelo de dominio con los datos nuevos
     * @param entity Entidad existente que será actualizada
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDomain(Event domain, @MappingTarget EventEntity entity);
}