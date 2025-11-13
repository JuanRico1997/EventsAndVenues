package com.riwi.h1.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para enviar datos de Eventos en las respuestas de la API.
 * Expone solo la información necesaria al cliente.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {

    /**
     * ID único del evento
     */
    private Long id;

    /**
     * Nombre del evento
     */
    private String name;

    /**
     * Descripción del evento
     */
    private String description;

    /**
     * Fecha y hora del evento
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    /**
     * ID del venue donde se realizará
     */
    private Long venueId;

    /**
     * Capacidad del evento
     */
    private Integer capacity;

    /**
     * Precio de la entrada
     */
    private Double ticketPrice;

    /**
     * Estado del evento (activo/inactivo)
     */
    private Boolean active;

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