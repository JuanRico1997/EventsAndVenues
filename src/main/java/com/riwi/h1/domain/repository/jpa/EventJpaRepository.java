package com.riwi.h1.domain.repository.jpa;

import com.riwi.h1.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para la entidad Event.
 *
 * Al extender JpaRepository, Spring Data JPA proporciona automáticamente:
 * - save(Event): Guardar o actualizar un evento
 * - findById(Long): Buscar por ID
 * - findAll(): Listar todos los eventos
 * - deleteById(Long): Eliminar por ID
 * - existsById(Long): Verificar si existe por ID
 * - count(): Contar total de registros
 * - Y muchos más métodos...
 *
 * Podemos añadir métodos personalizados siguiendo las convenciones de nombre
 * de Spring Data JPA y se implementarán automáticamente.
 */
@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {

    /**
     * Busca eventos por el ID del venue (lugar).
     * Spring Data JPA genera automáticamente la query:
     * SELECT * FROM events WHERE venue_id = ?
     *
     * @param venueId ID del venue
     * @return Lista de eventos del venue
     */
    List<Event> findByVenueId(Long venueId);

    /**
     * Busca eventos por nombre (ignorando mayúsculas/minúsculas).
     * Query generada: SELECT * FROM events WHERE LOWER(name) = LOWER(?)
     *
     * @param name Nombre del evento
     * @return Lista de eventos con ese nombre
     */
    List<Event> findByNameIgnoreCase(String name);

    /**
     * Busca eventos activos.
     * Query generada: SELECT * FROM events WHERE active = ?
     *
     * @param active Estado de activo/inactivo
     * @return Lista de eventos según el estado
     */
    List<Event> findByActive(Boolean active);

    /**
     * Busca eventos cuya fecha sea posterior a la fecha especificada.
     * Query generada: SELECT * FROM events WHERE event_date > ?
     *
     * @param date Fecha de referencia
     * @return Lista de eventos futuros
     */
    List<Event> findByEventDateAfter(LocalDateTime date);

    /**
     * Busca eventos cuya fecha esté entre dos fechas.
     * Query generada: SELECT * FROM events WHERE event_date BETWEEN ? AND ?
     *
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de eventos en el rango de fechas
     */
    List<Event> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Verifica si existe un evento con un nombre específico (ignorando mayúsculas).
     * Query generada: SELECT COUNT(*) > 0 FROM events WHERE LOWER(name) = LOWER(?)
     *
     * @param name Nombre del evento
     * @return true si existe, false si no
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Cuenta eventos por venue.
     * Query generada: SELECT COUNT(*) FROM events WHERE venue_id = ?
     *
     * @param venueId ID del venue
     * @return Cantidad de eventos del venue
     */
    long countByVenueId(Long venueId);
}