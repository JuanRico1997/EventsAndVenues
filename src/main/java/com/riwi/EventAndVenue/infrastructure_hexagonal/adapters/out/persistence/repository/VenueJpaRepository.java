package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para VenueEntity.
 *
 * Spring Data JPA genera automáticamente las implementaciones de estos métodos.
 * Esta interfaz NO se expone al dominio, solo se usa en la capa de infraestructura.
 */
@Repository
public interface VenueJpaRepository extends JpaRepository<VenueEntity, Long>, JpaSpecificationExecutor<VenueEntity> {

    /**
     * Busca venues por estado activo/inactivo.
     *
     * @param active Estado activo (true) o inactivo (false)
     * @return Lista de venues según el estado
     */
    List<VenueEntity> findByActive(Boolean active);

    /**
     * Busca venues por ubicación ignorando mayúsculas/minúsculas.
     *
     * @param location Ubicación a buscar
     * @return Lista de venues en esa ubicación
     */
    List<VenueEntity> findByLocationIgnoreCase(String location);

    /**
     * Verifica si existe un venue con el nombre dado (ignorando mayúsculas).
     *
     * @param name Nombre del venue
     * @return true si existe, false si no
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Busca venues con capacidad mayor o igual a la especificada.
     *
     * @param capacity Capacidad mínima
     * @return Lista de venues que cumplen el criterio
     */
    List<VenueEntity> findByCapacityGreaterThanEqual(Integer capacity);

    /**
     * Busca venues por ubicación usando JPQL (case-insensitive).
     *
     * @param location Ubicación a buscar
     * @return Lista de venues que contienen la ubicación
     */
    @Query("SELECT v FROM VenueEntity v WHERE LOWER(v.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<VenueEntity> findByLocationContainingIgnoreCase(@Param("location") String location);

    /**
     * Busca venues con capacidad en un rango.
     *
     * @param minCapacity Capacidad mínima
     * @param maxCapacity Capacidad máxima
     * @return Lista de venues en el rango de capacidad
     */
    @Query("SELECT v FROM VenueEntity v WHERE v.capacity BETWEEN :minCapacity AND :maxCapacity")
    List<VenueEntity> findByCapacityBetween(@Param("minCapacity") Integer minCapacity,
                                            @Param("maxCapacity") Integer maxCapacity);

    /**
     * Busca venues activos con al menos un evento activo.
     * Usa join para filtrar solo venues que tienen eventos.
     *
     * @return Lista de venues activos con eventos
     */
    @Query("SELECT DISTINCT v FROM VenueEntity v JOIN v.events e WHERE v.active = true AND e.active = true")
    List<VenueEntity> findActiveVenuesWithActiveEvents();

}