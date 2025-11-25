package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.specification;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.VenueEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications para construir consultas dinámicas de VenueEntity.
 *
 * Cada método retorna un Specification que puede ser combinado con otros
 * usando métodos como and(), or(), not().
 *
 * Ejemplo de uso:
 * Specification<VenueEntity> spec = VenueSpecifications.hasLocation("Medellín")
 *     .and(VenueSpecifications.hasMinCapacity(1000))
 *     .and(VenueSpecifications.isActive(true));
 */
public class VenueSpecifications {

    /**
     * Filtra venues por ubicación (búsqueda parcial, case-insensitive).
     *
     * @param location Ubicación o parte de la ubicación a buscar
     * @return Specification que filtra por LIKE %location%
     */
    public static Specification<VenueEntity> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null || location.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    "%" + location.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtra venues con capacidad mínima.
     *
     * @param minCapacity Capacidad mínima requerida
     * @return Specification que filtra por capacity >= minCapacity
     */
    public static Specification<VenueEntity> hasMinCapacity(Integer minCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (minCapacity == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), minCapacity);
        };
    }

    /**
     * Filtra venues con capacidad máxima.
     *
     * @param maxCapacity Capacidad máxima permitida
     * @return Specification que filtra por capacity <= maxCapacity
     */
    public static Specification<VenueEntity> hasMaxCapacity(Integer maxCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (maxCapacity == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("capacity"), maxCapacity);
        };
    }

    /**
     * Filtra venues por estado activo/inactivo.
     *
     * @param active true para venues activos, false para inactivos, null para todos
     * @return Specification que filtra por active
     */
    public static Specification<VenueEntity> isActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    /**
     * Filtra venues por nombre (búsqueda parcial, case-insensitive).
     *
     * @param name Nombre o parte del nombre a buscar
     * @return Specification que filtra por LIKE %name%
     */
    public static Specification<VenueEntity> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    /**
     * Filtra venues que tienen al menos un evento.
     *
     * @return Specification que filtra venues con eventos
     */
    public static Specification<VenueEntity> hasEvents() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotEmpty(root.get("events"));
    }

    /**
     * Filtra venues que NO tienen eventos.
     *
     * @return Specification que filtra venues sin eventos
     */
    public static Specification<VenueEntity> hasNoEvents() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isEmpty(root.get("events"));
    }
}
