package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

/**
 * Validador para @FutureOrNull.
 * Valida que una fecha sea futura o nula.
 */
public class FutureOrNullValidator implements ConstraintValidator<FutureOrNull, LocalDateTime> {

    @Override
    public void initialize(FutureOrNull constraintAnnotation) {
        // Inicialización si es necesaria
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        // Si es null, es válido (permite null)
        if (value == null) {
            return true;
        }

        // Si tiene valor, debe ser una fecha futura
        return value.isAfter(LocalDateTime.now());
    }
}