package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Validación personalizada para fechas futuras o nulas.
 * Permite que el campo sea null, pero si tiene valor, debe ser una fecha futura.
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureOrNullValidator.class)
@Documented
public @interface FutureOrNull {

    String message() default "{event.eventDate.future}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}