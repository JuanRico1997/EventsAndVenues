package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.mapper;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request.VenueRequest;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response.VenueResponse;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper REST de MapStruct para convertir entre DTOs y Venue (dominio).
 *
 * MapStruct genera automáticamente la implementación en tiempo de compilación.
 *
 * Conversiones:
 * - VenueRequest → Venue (para crear/actualizar)
 * - Venue → VenueResponse (para respuestas)
 *
 * componentModel = "spring" - Hace que Spring detecte el mapper como @Component
 */
@Mapper(componentModel = "spring")
public interface VenueRestMapper {

    /**
     * Convierte de VenueRequest (DTO) a Venue (dominio).
     *
     * Usado cuando el cliente envía datos para crear o actualizar un venue.
     *
     * @param request DTO con los datos del cliente
     * @return Modelo de dominio
     */
    Venue toDomain(VenueRequest request);

    /**
     * Convierte de Venue (dominio) a VenueResponse (DTO).
     *
     * Usado cuando devolvemos un venue al cliente.
     *
     * @param domain Modelo de dominio
     * @return DTO de respuesta
     */
    VenueResponse toResponse(Venue domain);

    /**
     * Convierte una lista de Venue a lista de VenueResponse.
     *
     * Usado en endpoints que devuelven múltiples venues (findAll, etc.)
     *
     * @param domains Lista de modelos de dominio
     * @return Lista de DTOs de respuesta
     */
    List<VenueResponse> toResponseList(List<Venue> domains);
}
