package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de entrada para buscar eventos con filtros dinámicos.
 *
 * Permite realizar búsquedas complejas combinando múltiples criterios.
 */
public interface SearchEventsUseCase {

    /**
     * Busca eventos aplicando múltiples filtros opcionales.
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
    List<Event> execute(Long venueId, Boolean active, LocalDateTime startDate,
                        LocalDateTime endDate, String name, Integer minCapacity,
                        Double maxPrice);
}