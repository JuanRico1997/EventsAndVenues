package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manejador global de excepciones con @ControllerAdvice.
 *
 * Captura excepciones de toda la aplicación y las convierte en respuestas
 * ErrorResponse estandarizadas siguiendo RFC 7807.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de Bean Validation.
     * Se dispara cuando @Valid falla en un @RequestBody.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        // Extraer errores de validación
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Los datos proporcionados no cumplen con las validaciones requeridas")
                .instance(request.getRequestURI())
                .traceId(traceId)
                .errors(errors)
                .build();

        log.warn("VALIDATION_ERROR traceId={} path={} errors={}",
                traceId, request.getRequestURI(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja EntityNotFoundException (cuando no se encuentra un recurso).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
            EntityNotFoundException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.warn("NOT_FOUND traceId={} path={} message={}",
                traceId, request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Maneja violaciones de integridad de datos (constraints de BD).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        // Extraer mensaje más específico si es posible
        String detail = "Violación de restricción de integridad en la base de datos";
        if (ex.getRootCause() != null && ex.getRootCause().getMessage() != null) {
            String rootMessage = ex.getRootCause().getMessage();
            if (rootMessage.contains("Unique")) {
                detail = "Ya existe un registro con estos datos";
            } else if (rootMessage.contains("foreign key")) {
                detail = "Referencia a un registro inexistente";
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Data Integrity Violation")
                .status(HttpStatus.CONFLICT.value())
                .detail(detail)
                .instance(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.error("DATA_INTEGRITY_ERROR traceId={} path={} message={}",
                traceId, request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Maneja IllegalArgumentException (argumentos inválidos).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Invalid Argument")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.warn("INVALID_ARGUMENT traceId={} path={} message={}",
                traceId, request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Maneja errores de autorización (403 Forbidden).
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(
            AuthorizationDeniedException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Access Denied")
                .status(HttpStatus.FORBIDDEN.value())
                .detail("No tienes permisos suficientes para realizar esta operación")
                .instance(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.warn("ACCESS_DENIED traceId={} path={} message={}",
                traceId, request.getRequestURI(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Maneja cualquier excepción no controlada (500 Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .type("about:blank")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("Ocurrió un error inesperado en el servidor. Por favor, contacte al administrador.")
                .instance(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.error("INTERNAL_ERROR traceId={} path={} exception={} message={}",
                traceId, request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Genera un traceId único para correlacionar logs con respuestas.
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}