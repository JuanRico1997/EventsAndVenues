package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.specification;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Specifications para construir consultas dinámicas de EventEntity.
 *
 * Cada método retorna un Specification que puede ser combinado con otros
 * usando métodos como and(), or(), not().
 *
 * Ejemplo de uso:
 * Specification<EventEntity> spec = EventSpecifications.hasVenue(1L)
 *     .and(EventSpecifications.isActive(true))
 *     .and(EventSpecifications.isAfterDate(LocalDateTime.now()));
 */
public class EventSpecifications {

    /**
     * Filtra eventos por venue específico.
     *
     * @param venueId ID del venue
     * @return Specification que filtra por venue_id
     */
    public static Specification<EventEntity> hasVenue(Long venueId) {
        return (root, query, criteriaBuilder) -> {
            if (venueId == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.equal(root.get("venue").get("id"), venueId);
        };
    }

    /**
     * Filtra eventos por estado activo/inactivo.
     *
     * @param active true para eventos activos, false para inactivos, null para todos
     * @return Specification que filtra por active
     */
    public static Specification<EventEntity> hasStatus(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    /**
     * Filtra eventos que ocurren después de una fecha específica.
     *
     * @param date Fecha límite (eventos después de esta fecha)
     * @return Specification que filtra por event_date > date
     */
    public static Specification<EventEntity> isAfterDate(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), date);
        };
    }

    /**
     * Filtra eventos que ocurren antes de una fecha específica.
     *
     * @param date Fecha límite (eventos antes de esta fecha)
     * @return Specification que filtra por event_date < date
     */
    public static Specification<EventEntity> isBeforeDate(LocalDateTime date) {
        return (root, query, criteriaBuilder) -> {
            if (date == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), date);
        };
    }

    /**
     * Filtra eventos que ocurren dentro de un rango de fechas.
     *
     * @param startDate Fecha inicial del rango
     * @param endDate Fecha final del rango
     * @return Specification que filtra por startDate <= event_date <= endDate
     */
    public static Specification<EventEntity> isBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            if (startDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), endDate);
            }
            if (endDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), startDate);
            }
            return criteriaBuilder.between(root.get("eventDate"), startDate, endDate);
        };
    }

    /**
     * Filtra eventos por nombre (búsqueda parcial, case-insensitive).
     *
     * @param name Nombre o parte del nombre a buscar
     * @return Specification que filtra por LIKE %name%
     */
    public static Specification<EventEntity> nameContains(String name) {
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
     * Filtra eventos con capacidad mínima.
     *
     * @param minCapacity Capacidad mínima requerida
     * @return Specification que filtra por capacity >= minCapacity
     */
    public static Specification<EventEntity> hasMinCapacity(Integer minCapacity) {
        return (root, query, criteriaBuilder) -> {
            if (minCapacity == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), minCapacity);
        };
    }

    /**
     * Filtra eventos con precio máximo de ticket.
     *
     * @param maxPrice Precio máximo del ticket
     * @return Specification que filtra por ticketPrice <= maxPrice
     */
    public static Specification<EventEntity> hasMaxPrice(Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (maxPrice == null) {
                return criteriaBuilder.conjunction(); // true (no filter)
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("ticketPrice"), maxPrice);
        };
    }
}