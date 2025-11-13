package com.riwi.h1.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos de Venues en peticiones POST y PUT.
 * Incluye validaciones para garantizar la integridad de los datos.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequest {

    /**
     * Nombre del venue (obligatorio)
     */
    @NotBlank(message = "Venue name is required")
    @Size(min = 3, max = 100, message = "Venue name must be between 3 and 100 characters")
    private String name;

    /**
     * Dirección del venue (obligatoria)
     */
    @NotBlank(message = "Address is required")
    @Size(min = 5, max = 200, message = "Address must be between 5 and 200 characters")
    private String address;

    /**
     * Ciudad donde se encuentra (obligatoria)
     */
    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be between 2 and 100 characters")
    private String city;

    /**
     * País donde se encuentra
     */
    @Size(min = 2, max = 100, message = "Country must be between 2 and 100 characters")
    private String country;

    /**
     * Capacidad máxima del venue (obligatoria)
     */
    @NotNull(message = "Max capacity is required")
    @Positive(message = "Max capacity must be positive")
    @Max(value = 500000, message = "Max capacity cannot exceed 500,000")
    private Integer maxCapacity;

    /**
     * Tipo de venue (ej: Teatro, Estadio, Club)
     */
    @Size(max = 50, message = "Type cannot exceed 50 characters")
    private String type;

    /**
     * Disponibilidad del venue
     */
    private Boolean available;
}