package com.riwi.EventAndVenue.domain_old.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Evento en el sistema de catálogo.
 * Esta clase se mapea a la tabla "events" en la base de datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    /**
     * ID único del evento (clave primaria).
     * Se genera automáticamente usando estrategia IDENTITY (auto-incremento en H2).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del evento.
     * No puede ser nulo y tiene un límite de 100 caracteres.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Descripción detallada del evento.
     * Puede almacenar texto largo (hasta 500 caracteres).
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Fecha y hora en que se realizará el evento.
     * No puede ser nulo.
     */
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    /**
     * ID del venue (lugar) donde se realizará el evento.
     * Relación simple por ID (sin mapeo JPA completo por ahora).
     */
    @Column(name = "venue_id")
    private Long venueId;

    /**
     * Capacidad máxima de asistentes al evento.
     * Debe ser un número positivo.
     */
    @Column(name = "capacity")
    private Integer capacity;

    /**
     * Precio del ticket de entrada al evento.
     */
    @Column(name = "ticket_price")
    private Double ticketPrice;

    /**
     * Indica si el evento está activo (disponible para venta).
     * Por defecto es true.
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * Fecha y hora de creación del registro.
     * Se establece automáticamente al crear la entidad.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     * Se actualiza automáticamente cada vez que se modifica la entidad.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Método que se ejecuta automáticamente antes de persistir (guardar por primera vez).
     * Establece las fechas de creación y actualización.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // Por defecto, el evento se crea activo
        if (this.active == null) {
            this.active = true;
        }
    }

    /**
     * Método que se ejecuta automáticamente antes de actualizar la entidad.
     * Actualiza la fecha de última modificación.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}