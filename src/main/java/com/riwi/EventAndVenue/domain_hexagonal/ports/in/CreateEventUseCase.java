package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;

/**
 * Puerto de entrada para el caso de uso: Crear Evento.
 *
 * Define el contrato para crear un nuevo evento con validaciones de negocio.
 * Será implementado en la capa de aplicación.
 */
public interface CreateEventUseCase {

    /**
     * Crea un nuevo evento en el sistema.
     *
     * Reglas de negocio:
     * - El nombre no puede estar vacío
     * - No puede existir otro evento con el mismo nombre
     * - La fecha debe ser futura
     * - Si tiene venueId, el venue debe existir
     * - La capacidad debe ser positiva
     * - El precio no puede ser negativo
     *
     * @param event Evento a crear (sin ID)
     * @return El evento creado con su ID generado
     * @throws IllegalArgumentException si las validaciones fallan
     */
    Event execute(Event event);
}