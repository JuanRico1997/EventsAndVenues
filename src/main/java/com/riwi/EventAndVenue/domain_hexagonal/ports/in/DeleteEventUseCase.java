package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

/**
 * Puerto de entrada para el caso de uso: Eliminar Evento.
 *
 * Define el contrato para eliminar un evento del sistema.
 * Será implementado en la capa de aplicación.
 */
public interface DeleteEventUseCase {

    /**
     * Elimina un evento por su ID.
     *
     * Reglas de negocio:
     * - El evento debe existir
     *
     * @param id ID del evento a eliminar
     * @throws IllegalArgumentException si el evento no existe
     */
    void execute(Long id);
}