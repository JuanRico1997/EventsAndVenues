package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;

/**
 * Puerto de entrada para el caso de uso: Crear Venue.
 *
 * Define el contrato para crear un nuevo venue con validaciones de negocio.
 * Será implementado en la capa de aplicación.
 */
public interface CreateVenueUseCase {

    /**
     * Crea un nuevo venue en el sistema.
     *
     * Reglas de negocio:
     * - El nombre no puede estar vacío
     * - No puede existir otro venue con el mismo nombre
     * - La ubicación no puede estar vacía
     * - La capacidad debe ser positiva
     *
     * @param venue Venue a crear (sin ID)
     * @return El venue creado con su ID generado
     * @throws IllegalArgumentException si las validaciones fallan
     */
    Venue execute(Venue venue);
}