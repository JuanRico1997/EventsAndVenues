package com.riwi.EventAndVenue.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuestas de error estandarizadas.
 * Proporciona una estructura consistente para todos los errores de la API.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp de cuándo ocurrió el error
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Código de estado HTTP
     */
    private Integer status;

    /**
     * Nombre del error HTTP (ej: "Not Found", "Bad Request")
     */
    private String error;

    /**
     * Mensaje descriptivo del error
     */
    private String message;

    /**
     * Ruta del endpoint donde ocurrió el error
     */
    private String path;

    /**
     * Lista de errores de validación (opcional, para múltiples errores)
     */
    private List<String> validationErrors;
}