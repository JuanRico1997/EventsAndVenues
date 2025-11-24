package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.mapper;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request.EventRequest;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response.EventResponse;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper REST de MapStruct para convertir entre DTOs y Event (dominio).
 *
 * MapStruct genera automáticamente la implementación en tiempo de compilación.
 *
 * Conversiones:
 * - EventRequest → Event (para crear/actualizar)
 * - Event → EventResponse (para respuestas)
 *
 * componentModel = "spring" - Hace que Spring detecte el mapper como @Component
 */
@Mapper(componentModel = "spring")
public interface EventRestMapper {

    /**
     * Convierte de EventRequest (DTO) a Event (dominio).
     *
     * Usado cuando el cliente envía datos para crear o actualizar un evento.
     *
     * @param request DTO con los datos del cliente
     * @return Modelo de dominio
     */
    Event toDomain(EventRequest request);

    /**
     * Convierte de Event (dominio) a EventResponse (DTO).
     *
     * Usado cuando devolvemos un evento al cliente.
     *
     * @param domain Modelo de dominio
     * @return DTO de respuesta
     */
    EventResponse toResponse(Event domain);

    /**
     * Convierte una lista de Event a lista de EventResponse.
     *
     * Usado en endpoints que devuelven múltiples eventos (findAll, etc.)
     *
     * @param domains Lista de modelos de dominio
     * @return Lista de DTOs de respuesta
     */
    List<EventResponse> toResponseList(List<Event> domains);
}