package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;

/**
 * Puerto de entrada para el caso de uso: Actualizar Venue.
 *
 * Define el contrato para actualizar un venue existente con validaciones de negocio.
 * Será implementado en la capa de aplicación.
 */
public interface UpdateVenueUseCase {

    /**
     * Actualiza un venue existente.
     *
     * Reglas de negocio:
     * - El venue debe existir
     * - Si cambia el nombre, no puede duplicarse con otro venue
     * - La ubicación no puede estar vacía
     * - La capacidad debe ser positiva
     *
     * @param id ID del venue a actualizar
     * @param venue Datos actualizados del venue
     * @return El venue actualizado
     * @throws IllegalArgumentException si las validaciones fallan
     */
    Venue execute(Long id, Venue venue);
}