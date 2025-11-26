package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.mapper;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.EventEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper de MapStruct para convertir entre Event (dominio) y EventEntity (JPA).
 *
 * Maneja la conversión entre venueId (dominio) y venue (entidad JPA).
 */
@Mapper(componentModel = "spring")
public interface EventJpaMapper {

    /**
     * Convierte de EventEntity (JPA) a Event (dominio).
     * Extrae el ID del venue de la entidad.
     */
    @Mapping(target = "venueId", source = "venue", qualifiedByName = "venueToVenueId")
    Event toDomain(EventEntity entity);

    /**
     * Convierte de Event (dominio) a EventEntity (JPA).
     * Crea una referencia de venue solo con el ID.
     */
    @Mapping(target = "venue", source = "venueId", qualifiedByName = "venueIdToVenue")
    EventEntity toEntity(Event domain);

    /**
     * Convierte una lista de EventEntity a lista de Event.
     */
    List<Event> toDomainList(List<EventEntity> entities);

    /**
     * Convierte una lista de Event a lista de EventEntity.
     */
    List<EventEntity> toEntityList(List<Event> domains);

    /**
     * Actualiza una entidad existente con datos del dominio.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "venue", source = "venueId", qualifiedByName = "venueIdToVenue")
    void updateEntityFromDomain(Event domain, @MappingTarget EventEntity entity);

    /**
     * Extrae el ID del venue de la entidad.
     * Si el venue es null, retorna null.
     */
    @Named("venueToVenueId")
    default Long venueToVenueId(VenueEntity venue) {
        return venue != null ? venue.getId() : null;
    }

    /**
     * Crea una referencia de VenueEntity con solo el ID.
     * No carga el venue completo de la base de datos.
     */
    @Named("venueIdToVenue")
    default VenueEntity venueIdToVenue(Long venueId) {
        if (venueId == null) {
            return null;
        }
        VenueEntity venue = new VenueEntity();
        venue.setId(venueId);
        return venue;
    }
}