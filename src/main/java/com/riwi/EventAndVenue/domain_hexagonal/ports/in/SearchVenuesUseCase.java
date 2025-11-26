package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;

import java.util.List;

/**
 * Puerto de entrada para buscar venues con filtros dinámicos.
 *
 * Permite realizar búsquedas complejas combinando múltiples criterios.
 */
public interface SearchVenuesUseCase {

    /**
     * Busca venues aplicando múltiples filtros opcionales.
     *
     * @param location Filtro por ubicación parcial (opcional)
     * @param minCapacity Filtro por capacidad mínima (opcional)
     * @param maxCapacity Filtro por capacidad máxima (opcional)
     * @param active Filtro por estado activo/inactivo (opcional)
     * @param name Filtro por nombre parcial (opcional)
     * @param hasEvents Filtro por venues con/sin eventos (opcional)
     * @return Lista de venues que cumplen los filtros
     */
    List<Venue> execute(String location, Integer minCapacity, Integer maxCapacity,
                        Boolean active, String name, Boolean hasEvents);
}