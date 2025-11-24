package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para VenueEntity.
 *
 * Spring Data JPA genera automáticamente las implementaciones de estos métodos.
 * Esta interfaz NO se expone al dominio, solo se usa en la capa de infraestructura.
 */
@Repository
public interface VenueJpaRepository extends JpaRepository<VenueEntity, Long> {

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
}