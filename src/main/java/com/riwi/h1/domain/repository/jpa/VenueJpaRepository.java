package com.riwi.h1.domain.repository.jpa;

import com.riwi.h1.domain.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Venue.
 *
 * Al extender JpaRepository, Spring Data JPA proporciona automáticamente:
 * - save(Venue): Guardar o actualizar un venue
 * - findById(Long): Buscar por ID
 * - findAll(): Listar todos los venues
 * - deleteById(Long): Eliminar por ID
 * - existsById(Long): Verificar si existe por ID
 * - count(): Contar total de registros
 * - Y muchos más métodos...
 *
 * Podemos añadir métodos personalizados siguiendo las convenciones de nombre
 * de Spring Data JPA y se implementarán automáticamente.
 */
@Repository
public interface VenueJpaRepository extends JpaRepository<Venue, Long> {

    /**
     * Busca venues por ciudad.
     * Spring Data JPA genera automáticamente la query:
     * SELECT * FROM venues WHERE city = ?
     *
     * @param city Ciudad del venue
     * @return Lista de venues en esa ciudad
     */
    List<Venue> findByCity(String city);

    /**
     * Busca venues por país.
     * Query generada: SELECT * FROM venues WHERE country = ?
     *
     * @param country País del venue
     * @return Lista de venues en ese país
     */
    List<Venue> findByCountry(String country);

    /**
     * Busca venues por tipo (teatro, estadio, etc.).
     * Query generada: SELECT * FROM venues WHERE type = ?
     *
     * @param type Tipo de venue
     * @return Lista de venues de ese tipo
     */
    List<Venue> findByType(String type);

    /**
     * Busca venues disponibles o no disponibles.
     * Query generada: SELECT * FROM venues WHERE available = ?
     *
     * @param available Estado de disponibilidad
     * @return Lista de venues según disponibilidad
     */
    List<Venue> findByAvailable(Boolean available);

    /**
     * Busca venues por nombre (ignorando mayúsculas/minúsculas).
     * Query generada: SELECT * FROM venues WHERE LOWER(name) = LOWER(?)
     *
     * @param name Nombre del venue
     * @return Lista de venues con ese nombre
     */
    List<Venue> findByNameIgnoreCase(String name);

    /**
     * Busca venues cuya capacidad máxima sea mayor o igual a un valor.
     * Query generada: SELECT * FROM venues WHERE max_capacity >= ?
     *
     * @param capacity Capacidad mínima requerida
     * @return Lista de venues que cumplen la capacidad
     */
    List<Venue> findByMaxCapacityGreaterThanEqual(Integer capacity);

    /**
     * Busca venues por ciudad y disponibilidad.
     * Query generada: SELECT * FROM venues WHERE city = ? AND available = ?
     *
     * @param city Ciudad del venue
     * @param available Estado de disponibilidad
     * @return Lista de venues disponibles en la ciudad
     */
    List<Venue> findByCityAndAvailable(String city, Boolean available);

    /**
     * Verifica si existe un venue con un nombre específico (ignorando mayúsculas).
     * Query generada: SELECT COUNT(*) > 0 FROM venues WHERE LOWER(name) = LOWER(?)
     *
     * @param name Nombre del venue
     * @return true si existe, false si no
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Cuenta venues por ciudad.
     * Query generada: SELECT COUNT(*) FROM venues WHERE city = ?
     *
     * @param city Ciudad
     * @return Cantidad de venues en la ciudad
     */
    long countByCity(String city);

    /**
     * Cuenta venues disponibles.
     * Query generada: SELECT COUNT(*) FROM venues WHERE available = ?
     *
     * @param available Estado de disponibilidad
     * @return Cantidad de venues disponibles
     */
    long countByAvailable(Boolean available);
}