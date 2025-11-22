package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;

/**
 * Puerto de entrada para el caso de uso: Actualizar Evento.
 *
 * Define el contrato para actualizar un evento existente con validaciones de negocio.
 * Será implementado en la capa de aplicación.
 */
public interface UpdateEventUseCase {

    /**
     * Actualiza un evento existente.
     *
     * Reglas de negocio:
     * - El evento debe existir
     * - Si cambia el nombre, no puede duplicarse con otro evento
     * - La fecha debe ser futura
     * - Si cambia venueId, el venue debe existir
     * - La capacidad debe ser positiva
     * - El precio no puede ser negativo
     *
     * @param id ID del evento a actualizar
     * @param event Datos actualizados del evento
     * @return El evento actualizado
     * @throws IllegalArgumentException si las validaciones fallan
     */
    Event execute(Long id, Event event);
}