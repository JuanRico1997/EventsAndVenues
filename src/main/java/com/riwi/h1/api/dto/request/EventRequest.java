package com.riwi.h1.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para recibir datos de Eventos en peticiones POST y PUT.
 * Incluye validaciones para garantizar la integridad de los datos.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    /**
     * Nombre del evento (obligatorio)
     */
    @NotBlank(message = "Event name is required")
    @Size(min = 3, max = 100, message = "Event name must be between 3 and 100 characters")
    private String name;

    /**
     * Descripción del evento
     */
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    /**
     * Fecha y hora del evento (obligatoria)
     */
    @NotNull(message = "Event date is required")
    @Future(message = "Event date must be in the future")
    private LocalDateTime eventDate;

    /**
     * ID del venue donde se realizará el evento (obligatorio)
     */
    @NotNull(message = "Venue ID is required")
    @Positive(message = "Venue ID must be positive")
    private Long venueId;

    /**
     * Capacidad del evento
     */
    @Positive(message = "Capacity must be positive")
    @Max(value = 100000, message = "Capacity cannot exceed 100,000")
    private Integer capacity;

    /**
     * Precio de la entrada
     */
    @NotNull(message = "Ticket price is required")
    @PositiveOrZero(message = "Ticket price cannot be negative")
    @DecimalMax(value = "10000000.0", message = "Ticket price cannot exceed 10,000,000")
    private Double ticketPrice;

    /**
     * Estado del evento (activo/inactivo)
     */
    private Boolean active;
}