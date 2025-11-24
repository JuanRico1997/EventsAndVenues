package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request;

import jakarta.validation.constraints.*;

/**
 * DTO para recibir datos de Venue desde el cliente.
 *
 * Contiene validaciones para asegurar que los datos sean correctos.
 * Se usa en los endpoints POST y PUT.
 */
public class VenueRequest {

    @NotBlank(message = "El nombre del venue es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    private String location;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    private Boolean active;

    // Constructor vacío
    public VenueRequest() {
    }

    // Constructor completo
    public VenueRequest(String name, String location, Integer capacity,
                        String description, Boolean active) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.active = active;
    }

    // GETTERS Y SETTERS

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
}