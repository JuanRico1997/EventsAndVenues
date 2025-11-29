package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnCreate;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnUpdate;
import jakarta.validation.constraints.*;

/**
 * DTO de entrada para Venue con validaciones avanzadas y grupos.
 */
public class VenueRequest {

    @NotBlank(message = "{venue.name.required}", groups = {OnCreate.class})
    @Size(min = 3, max = 100, message = "{venue.name.size}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(message = "{venue.location.required}", groups = {OnCreate.class})
    @Size(min = 5, max = 200, message = "{venue.location.size}", groups = {OnCreate.class, OnUpdate.class})
    private String location;

    @Positive(message = "{venue.capacity.positive}", groups = {OnCreate.class, OnUpdate.class})
    private Integer capacity;

    @Size(max = 500, message = "{venue.description.size}", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    private Boolean active;

    // Constructor vacío
    public VenueRequest() {
    }

    // Constructor completo
    public VenueRequest(String name, String location, Integer capacity, String description, Boolean active) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.active = active;
    }

    // Getters y Setters
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