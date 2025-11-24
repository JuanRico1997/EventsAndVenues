package com.riwi.EventAndVenue.domain_hexagonal.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio puro para Venue.
 *
 * Esta clase representa un Venue (lugar/sede) en el sistema.
 * NO tiene anotaciones de JPA porque pertenece al dominio puro.
 */
public class Venue {

    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Venue() {
    }

    // Constructor completo
    public Venue(Long id, String name, String location, Integer capacity,
                 String description, Boolean active, LocalDateTime createdAt,
                 LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor para crear nuevo venue (sin ID ni timestamps)
    public Venue(String name, String location, Integer capacity,
                 String description, Boolean active) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.active = active;
    }

    // ========== GETTERS Y SETTERS ==========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", active=" + active +
                '}';
    }
}