package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.filter;

import java.time.LocalDateTime;

/**
 * DTO para encapsular criterios de búsqueda de eventos.
 *
 * Se usa en endpoints de búsqueda filtrada para construir
 * Specifications dinámicamente.
 *
 * Todos los campos son opcionales (pueden ser null).
 * Solo se aplican los filtros que tengan valor.
 */
public class EventFilter {

    private Long venueId;
    private Boolean active;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String name;
    private Integer minCapacity;
    private Double maxPrice;

    // Constructor vacío
    public EventFilter() {
    }

    // Constructor completo
    public EventFilter(Long venueId, Boolean active, LocalDateTime startDate,
                       LocalDateTime endDate, String name, Integer minCapacity,
                       Double maxPrice) {
        this.venueId = venueId;
        this.active = active;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.minCapacity = minCapacity;
        this.maxPrice = maxPrice;
    }

    // GETTERS Y SETTERS

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(Integer minCapacity) {
        this.minCapacity = minCapacity;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}