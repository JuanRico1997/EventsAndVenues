package com.riwi.EventAndVenue.domain_old.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un Venue (lugar/recinto) en el sistema de catálogo.
 * Esta clase se mapea a la tabla "venues" en la base de datos.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venues")
public class Venue {

    /**
     * ID único del venue (clave primaria).
     * Se genera automáticamente usando estrategia IDENTITY (auto-incremento en H2).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del venue.
     * No puede ser nulo y tiene un límite de 100 caracteres.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Dirección física del venue.
     * Puede almacenar hasta 200 caracteres.
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * Ciudad donde se ubica el venue.
     * No puede ser nulo y tiene un límite de 50 caracteres.
     */
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    /**
     * País donde se ubica el venue.
     * Tiene un límite de 50 caracteres.
     */
    @Column(name = "country", length = 50)
    private String country;

    /**
     * Capacidad máxima del venue (número de personas que puede albergar).
     * Debe ser un número positivo.
     */
    @Column(name = "max_capacity")
    private Integer maxCapacity;

    /**
     * Tipo de venue (ejemplo: teatro, estadio, sala de conferencias, etc.).
     * Tiene un límite de 50 caracteres.
     */
    @Column(name = "type", length = 50)
    private String type;

    /**
     * Indica si el venue está disponible para eventos.
     * Por defecto es true.
     */
    @Column(name = "available")
    private Boolean available;

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
        // Por defecto, el venue se crea disponible
        if (this.available == null) {
            this.available = true;
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