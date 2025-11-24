package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper de MapStruct para convertir entre Venue (dominio) y VenueEntity (JPA).
 *
 * MapStruct genera automáticamente la implementación en tiempo de compilación.
 * Solo definimos las interfaces de los métodos de conversión.
 *
 * componentModel = "spring" - Hace que Spring detecte el mapper como @Component
 */
@Mapper(componentModel = "spring")
public interface VenueJpaMapper {

    /**
     * Convierte de VenueEntity (JPA) a Venue (dominio).
     *
     * @param entity Entidad JPA
     * @return Modelo de dominio
     */
    Venue toDomain(VenueEntity entity);

    /**
     * Convierte de Venue (dominio) a VenueEntity (JPA).
     *
     * @param domain Modelo de dominio
     * @return Entidad JPA
     */
    VenueEntity toEntity(Venue domain);

    /**
     * Convierte una lista de VenueEntity a lista de Venue.
     *
     * @param entities Lista de entidades JPA
     * @return Lista de modelos de dominio
     */
    List<Venue> toDomainList(List<VenueEntity> entities);

    /**
     * Convierte una lista de Venue a lista de VenueEntity.
     *
     * @param domains Lista de modelos de dominio
     * @return Lista de entidades JPA
     */
    List<VenueEntity> toEntityList(List<Venue> domains);

    /**
     * Actualiza una entidad existente con datos del dominio.
     * Útil para operaciones de UPDATE sin perder el ID.
     *
     * @param domain Modelo de dominio con los datos nuevos
     * @param entity Entidad existente que será actualizada
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDomain(Venue domain, @MappingTarget VenueEntity entity);
}
