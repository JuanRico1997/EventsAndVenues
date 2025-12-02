package com.riwi.EventAndVenue.infrastructure_hexagonal.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de métricas personalizadas con Micrometer.
 */
@Configuration
public class MetricsConfig {

    /**
     * Contador de eventos creados.
     */
    @Bean
    public Counter eventsCreatedCounter(MeterRegistry registry) {
        return Counter.builder("events.created")
                .description("Total number of events created")
                .tag("type", "business")
                .register(registry);
    }

    /**
     * Contador de venues creados.
     */
    @Bean
    public Counter venuesCreatedCounter(MeterRegistry registry) {
        return Counter.builder("venues.created")
                .description("Total number of venues created")
                .tag("type", "business")
                .register(registry);
    }

    /**
     * Contador de eventos eliminados.
     */
    @Bean
    public Counter eventsDeletedCounter(MeterRegistry registry) {
        return Counter.builder("events.deleted")
                .description("Total number of events deleted")
                .tag("type", "business")
                .register(registry);
    }

    /**
     * Contador de venues eliminados.
     */
    @Bean
    public Counter venuesDeletedCounter(MeterRegistry registry) {
        return Counter.builder("venues.deleted")
                .description("Total number of venues deleted")
                .tag("type", "business")
                .register(registry);
    }

    /**
     * Contador de registros de usuarios.
     */
    @Bean
    public Counter usersRegisteredCounter(MeterRegistry registry) {
        return Counter.builder("users.registered")
                .description("Total number of users registered")
                .tag("type", "auth")
                .register(registry);
    }

    /**
     * Contador de login exitosos.
     */
    @Bean
    public Counter loginSuccessCounter(MeterRegistry registry) {
        return Counter.builder("login.success")
                .description("Total number of successful logins")
                .tag("type", "auth")
                .register(registry);
    }

    /**
     * Contador de login fallidos.
     */
    @Bean
    public Counter loginFailureCounter(MeterRegistry registry) {
        return Counter.builder("login.failure")
                .description("Total number of failed logins")
                .tag("type", "auth")
                .register(registry);
    }

    /**
     * Timer para medir tiempo de creación de eventos.
     */
    @Bean
    public Timer eventCreationTimer(MeterRegistry registry) {
        return Timer.builder("events.creation.time")
                .description("Time taken to create an event")
                .tag("type", "performance")
                .register(registry);
    }

    /**
     * Timer para medir tiempo de creación de venues.
     */
    @Bean
    public Timer venueCreationTimer(MeterRegistry registry) {
        return Timer.builder("venues.creation.time")
                .description("Time taken to create a venue")
                .tag("type", "performance")
                .register(registry);
    }
}