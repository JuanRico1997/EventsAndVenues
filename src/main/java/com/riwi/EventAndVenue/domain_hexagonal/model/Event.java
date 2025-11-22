package com.riwi.EventAndVenue.domain_hexagonal.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio puro para Event.
 *
 * Esta clase representa un Evento en el sistema.
 * NO tiene anotaciones de JPA porque pertenece al dominio puro.
 * La lógica de negocio se maneja en los casos de uso.
 */
public class Event {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private Long venueId;
    private Integer capacity;
    private Double ticketPrice;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public Event() {
    }

    // Constructor completo
    public Event(Long id, String name, String description, LocalDateTime eventDate,
                 Long venueId, Integer capacity, Double ticketPrice, Boolean active,
                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Constructor para crear nuevo evento (sin ID ni timestamps)
    public Event(String name, String description, LocalDateTime eventDate,
                 Long venueId, Integer capacity, Double ticketPrice, Boolean active) {
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
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
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eventDate=" + eventDate +
                ", active=" + active +
                '}';
    }
}