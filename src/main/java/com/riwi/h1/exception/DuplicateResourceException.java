package com.riwi.h1.exception;

public class DuplicateResourceException extends RuntimeException{
    /**
     * Constructor con mensaje personalizado.
     *
     * @param message Mensaje descriptivo del error
     */
    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Constructor con recurso y campo específico.
     *
     * @param resourceName Nombre del recurso (ej: "Event", "Venue")
     * @param fieldName Nombre del campo duplicado (ej: "name")
     * @param fieldValue Valor del campo duplicado
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceName, fieldName, fieldValue));
    }
}

