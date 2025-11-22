package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

/**
 * Puerto de entrada para el caso de uso: Eliminar Venue.
 *
 * Define el contrato para eliminar un venue del sistema.
 * Será implementado en la capa de aplicación.
 */
public interface DeleteVenueUseCase {

    /**
     * Elimina un venue por su ID.
     *
     * Reglas de negocio:
     * - El venue debe existir
     * - (Opcional) Verificar que no tenga eventos asociados
     *
     * @param id ID del venue a eliminar
     * @throws IllegalArgumentException si el venue no existe
     */
    void execute(Long id);
}

