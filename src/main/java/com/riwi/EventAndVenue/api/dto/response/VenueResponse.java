package com.riwi.EventAndVenue.api.dto.response;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para enviar datos de Venues en las respuestas de la API.
 * Expone solo la información necesaria al cliente.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {

    /**
     * ID único del venue
     */
    private Long id;

    /**
     * Nombre del venue
     */
    private String name;

    /**
     * Dirección física
     */
    private String address;

    /**
     * Ciudad
     */
    private String city;

    /**
     * País
     */
    private String country;

    /**
     * Capacidad máxima
     */
    private Integer maxCapacity;

    /**
     * Tipo de venue
     */
    private String type;

    /**
     * Disponibilidad
     */
    private Boolean available;

    /**
     * Fecha de creación del registro
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    /**
     * Fecha de última actualización
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
