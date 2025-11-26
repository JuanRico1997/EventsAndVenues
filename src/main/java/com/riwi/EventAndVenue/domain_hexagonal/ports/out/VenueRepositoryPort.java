package com.riwi.EventAndVenue.domain_hexagonal.ports.out;

import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para operaciones de persistencia de Venue.
 *
 * Esta interfaz define QUÉ operaciones necesita el dominio,
 * pero NO SABE CÓMO se implementan (puede ser JPA, MongoDB, API externa, etc.)
 *
 * Será implementada por un adaptador en la capa de infraestructura.
 */
public interface VenueRepositoryPort {

    /**
     * Guarda un venue (crear o actualizar).
     *
     * @param venue Venue a guardar
     * @return El venue guardado con su ID generado
     */
    Venue save(Venue venue);

    /**
     * Busca un venue por su ID.
     *
     * @param id ID del venue
     * @return Optional con el venue si existe, vacío si no
     */
    Optional<Venue> findById(Long id);

    /**
     * Obtiene todos los venues.
     *
     * @return Lista de todos los venues
     */
    List<Venue> findAll();

    /**
     * Elimina un venue por su ID.
     *
     * @param id ID del venue a eliminar
     */
    void deleteById(Long id);

    /**
     * Verifica si existe un venue por su ID.
     *
     * @param id ID del venue
     * @return true si existe, false si no
     */
    boolean existsById(Long id);

    /**
     * Busca venues activos o inactivos.
     *
     * @param active Estado activo (true) o inactivo (false)
     * @return Lista de venues según el estado
     */
    List<Venue> findByActive(Boolean active);

    /**
     * Busca venues por ubicación (ignorando mayúsculas).
     *
     * @param location Ubicación a buscar
     * @return Lista de venues en esa ubicación
     */
    List<Venue> findByLocationIgnoreCase(String location);

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
    List<Venue> findByCapacityGreaterThanEqual(Integer capacity);

    /**
     * Busca venues aplicando múltiples filtros dinámicos.
     *
     * @param location Filtro por ubicación parcial (opcional)
     * @param minCapacity Filtro por capacidad mínima (opcional)
     * @param maxCapacity Filtro por capacidad máxima (opcional)
     * @param active Filtro por estado activo/inactivo (opcional)
     * @param name Filtro por nombre parcial (opcional)
     * @param hasEvents Filtro por venues con/sin eventos (opcional)
     * @return Lista de venues que cumplen los filtros
     */
    List<Venue> findByFilters(String location, Integer minCapacity, Integer maxCapacity,
                              Boolean active, String name, Boolean hasEvents);
}


/**



        ##  **Explicación de los Puertos de Salida:**

        ### **¿Qué son?**
Son **contratos** (interfaces) que dicen:
        > "El dominio necesita estas operaciones para trabajar, pero NO le importa CÓMO se hacen"

        ### **¿Por qué son importantes?**
        1. **Inversión de dependencias** ⬆️
        - El dominio NO depende de JPA
   - JPA depende del dominio (implementa el puerto)

2. **Flexibilidad** 🔄
        - Hoy usas JPA → Mañana puedes cambiar a MongoDB
   - Solo cambias la implementación, NO el dominio

3. **Testeabilidad** 🧪
        - Puedes crear implementaciones falsas (mocks) para probar sin BD

### **Analogía:**
Es como un **enchufe eléctrico**:
        - El **puerto** (interfaz) es el enchufe en la pared
- El **adaptador** (implementación JPA) es el cargador que se conecta
- Puedes cambiar el cargador sin cambiar el enchufe


 **/