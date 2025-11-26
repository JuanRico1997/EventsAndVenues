package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.EventEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio Spring Data JPA para EventEntity.
 *
 * Spring Data JPA genera automáticamente las implementaciones de estos métodos.
 * Esta interfaz NO se expone al dominio, solo se usa en la capa de infraestructura.
 */
@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    /**
     * Busca eventos por venue ID.
     *
     * @param venueId ID del venue
     * @return Lista de eventos del venue
     */
    List<EventEntity> findByVenueId(Long venueId);

    /**
     * Busca eventos por nombre ignorando mayúsculas/minúsculas.
     *
     * @param name Nombre del evento
     * @return Lista de eventos con ese nombre
     */
    List<EventEntity> findByNameIgnoreCase(String name);

    /**
     * Busca eventos por estado activo/inactivo.
     *
     * @param active Estado activo (true) o inactivo (false)
     * @return Lista de eventos según el estado
     */
    List<EventEntity> findByActive(Boolean active);

    /**
     * Busca eventos cuya fecha sea posterior a la fecha dada.
     *
     * @param date Fecha de referencia
     * @return Lista de eventos futuros
     */
    List<EventEntity> findByEventDateAfter(LocalDateTime date);

    /**
     * Busca eventos cuya fecha esté entre dos fechas.
     *
     * @param startDate Fecha inicial
     * @param endDate Fecha final
     * @return Lista de eventos en el rango
     */
    List<EventEntity> findByEventDateBetween(LocalDateTime startDate, LocalDateTime endDate);

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

    /**
     * Busca eventos con su venue cargado (evita N+1).
     * Usa join fetch para cargar el venue en una sola consulta.
     */
    @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.active = true")
    List<EventEntity> findAllActiveWithVenue();

    /**
     * Busca eventos por venue usando join fetch.
     *
     * @param venueId ID del venue
     * @return Lista de eventos del venue con el venue cargado
     */
    @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue v WHERE v.id = :venueId")
    List<EventEntity> findByVenueIdWithVenue(@Param("venueId") Long venueId);

    /**
     * Busca eventos próximos (después de una fecha) con venue cargado.
     *
     * @param date Fecha límite
     * @return Lista de eventos próximos con venue cargado
     */
    @Query("SELECT e FROM EventEntity e JOIN FETCH e.venue WHERE e.eventDate >= :date ORDER BY e.eventDate ASC")
    List<EventEntity> findUpcomingEventsWithVenue(@Param("date") LocalDateTime date);

    /**
     * Cuenta eventos por venue sin cargar las entidades completas.
     *
     * @param venueId ID del venue
     * @return Cantidad de eventos
     */
    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.venue.id = :venueId")
    long countEventsByVenue(@Param("venueId") Long venueId);

    /**
     * Encuentra todos los eventos con sus venues cargados.
     * Usa @EntityGraph para evitar N+1 queries.
     */
    @EntityGraph(attributePaths = {"venue"})
    @Override
    List<EventEntity> findAll();

    /**
     * Encuentra eventos activos con sus venues cargados.
     * Combina @EntityGraph con una consulta personalizada.
     */
    @EntityGraph(attributePaths = {"venue"})
    List<EventEntity> findByActiveTrue();
}