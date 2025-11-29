package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Respuesta de error estandarizada siguiendo RFC 7807 (ProblemDetail).
 *
 * Incluye:
 * - type: URI que identifica el tipo de problema
 * - title: Resumen corto del problema
 * - status: Código HTTP
 * - detail: Explicación detallada
 * - instance: URI de la petición que causó el error
 * - timestamp: Momento del error
 * - traceId: Identificador único para correlacionar con logs
 * - errors: Mapa de errores de validación (opcional)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private LocalDateTime timestamp;
    private String traceId;
    private Map<String, String> errors; // Para errores de validación

    // Constructor vacío
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor builder-style
    public ErrorResponse(String type, String title, Integer status, String detail,
                         String instance, String traceId) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.timestamp = LocalDateTime.now();
        this.traceId = traceId;
    }

    // Getters y Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ErrorResponse errorResponse;

        public Builder() {
            this.errorResponse = new ErrorResponse();
        }

        public Builder type(String type) {
            errorResponse.type = type;
            return this;
        }

        public Builder title(String title) {
            errorResponse.title = title;
            return this;
        }

        public Builder status(Integer status) {
            errorResponse.status = status;
            return this;
        }

        public Builder detail(String detail) {
            errorResponse.detail = detail;
            return this;
        }

        public Builder instance(String instance) {
            errorResponse.instance = instance;
            return this;
        }

        public Builder traceId(String traceId) {
            errorResponse.traceId = traceId;
            return this;
        }

        public Builder errors(Map<String, String> errors) {
            errorResponse.errors = errors;
            return this;
        }

        public ErrorResponse build() {
            return errorResponse;
        }
    }
}