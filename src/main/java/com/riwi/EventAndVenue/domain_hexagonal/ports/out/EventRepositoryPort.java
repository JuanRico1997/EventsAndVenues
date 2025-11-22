package com.riwi.EventAndVenue.domain_hexagonal.ports.out;

import com.riwi.EventAndVenue.domain_hexagonal.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de persistencia de Event.
 *
 * Esta interfaz define QUÉ operaciones necesita el dominio,
 * pero NO SABE CÓMO se implementan (puede ser JPA, MongoDB, API externa, etc.)
 *
 * Será implementada por un adaptador en la capa de infraestructura.
 */
public interface EventRepositoryPort {

    /**
     * Guarda un evento (crear o actualizar).
     *
     * @param event Evento a guardar
     * @return El evento guardado con su ID generado
     */
    Event save(Event event);

    /**
     * Busca un evento por su ID.
     *
     * @param id ID del evento
     * @return Optional con el evento si existe, vacío si no
     */
    Optional<Event> findById(Long id);

    /**
     * Obtiene todos los eventos.
     *
     * @return Lista de todos los eventos
     */
    List<Event> findAll();

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un evento por su ID.
     *
     * @param id ID del evento
     * @return true si existe, false si no
     */
    boolean existsById(Long id);

    /**
     * Busca eventos por el ID del venue.
     *
     * @param venueId ID del venue
     * @return Lista de eventos del venue
     */
    List<Event> findByVenueId(Long venueId);

    /**
     * Busca eventos activos o inactivos.
     *
     * @param active Estado activo (true) o inactivo (false)
     * @return Lista de eventos según el estado
     */
    List<Event> findByActive(Boolean active);

    /**
     * Busca eventos cuya fecha sea posterior a la fecha dada.
     *
     * @param date Fecha de referencia
     * @return Lista de eventos futuros
     */
    List<Event> findByEventDateAfter(LocalDateTime date);

    /**
     * Busca eventos cuya fecha esté entre dos fechas.
     *
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de eventos en el rango
     */
    List<Event> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Verifica si existe un evento con el nombre dado (ignorando mayúsculas).
     *
     * @param name Nombre del evento
     * @return true si existe, false si no
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Cuenta eventos por venue.
     *
     * @param venueId ID del venue
     * @return Cantidad de eventos del venue
     */
    long countByVenueId(Long venueId);
}