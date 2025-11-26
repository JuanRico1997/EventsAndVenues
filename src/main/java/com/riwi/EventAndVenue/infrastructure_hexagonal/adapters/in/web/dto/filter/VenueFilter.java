package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.filter;

/**
 * DTO para encapsular criterios de búsqueda de venues.
 *
 * Se usa en endpoints de búsqueda filtrada para construir
 * Specifications dinámicamente.
 *
 * Todos los campos son opcionales (pueden ser null).
 * Solo se aplican los filtros que tengan valor.
 */
public class VenueFilter {

    private String location;
    private Integer minCapacity;
    private Integer maxCapacity;
    private Boolean active;
    private String name;
    private Boolean hasEvents;

    // Constructor vacío
    public VenueFilter() {
    }

    // Constructor completo
    public VenueFilter(String location, Integer minCapacity, Integer maxCapacity,
                       Boolean active, String name, Boolean hasEvents) {
        this.location = location;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.active = active;
        this.name = name;
        this.hasEvents = hasEvents;
    }

    // GETTERS Y SETTERS

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasEvents() {
        return hasEvents;
    }

    public void setHasEvents(Boolean hasEvents) {
        this.hasEvents = hasEvents;
    }
}
