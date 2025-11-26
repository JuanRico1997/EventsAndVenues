package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EventEntity> events = new ArrayList<>();

    public VenueEntity() {
    }

    public VenueEntity(Long id, String name, String location, Integer capacity,
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public List<EventEntity> getEvents() {return events;}

    public void setEvents(List<EventEntity> events) {this.events = events;}

    // MÉTODOS HELPER PARA MANEJAR LA RELACIÓN

    /**
     * Agrega un evento a este venue.
     * Mantiene la consistencia bidireccional de la relación.
     */
    public void addEvent(EventEntity event) {
        events.add(event);
        event.setVenue(this);
    }

    /**
     * Remueve un evento de este venue.
     * Mantiene la consistencia bidireccional de la relación.
     */
    public void removeEvent(EventEntity event) {
        events.remove(event);
        event.setVenue(null);
    }
}