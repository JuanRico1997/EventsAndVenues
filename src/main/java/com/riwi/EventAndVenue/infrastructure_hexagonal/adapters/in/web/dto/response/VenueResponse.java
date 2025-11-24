package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response;

import java.time.LocalDateTime;

/**
 * DTO para enviar datos de Venue al cliente.
 *
 * Contiene todos los datos del venue incluyendo ID y timestamps.
 * Se usa en las respuestas de todos los endpoints.
 */
public class VenueResponse {

    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public VenueResponse() {
    }

    // Constructor completo
    public VenueResponse(Long id, String name, String location, Integer capacity,
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

    // GETTERS Y SETTERS

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
}