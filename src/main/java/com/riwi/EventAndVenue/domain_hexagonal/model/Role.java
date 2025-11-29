package com.riwi.EventAndVenue.domain_hexagonal.model;

import java.time.LocalDateTime;

/**
 * Modelo de dominio para Role (Rol).
 *
 * Representa un rol de autorización en el sistema.
 * Ejemplos: ROLE_USER, ROLE_ADMIN, ROLE_ORGANIZER
 *
 * NO tiene dependencias de infraestructura.
 */
public class Role {

    private Long id;
    private String name; // ROLE_USER, ROLE_ADMIN, ROLE_ORGANIZER
    private String description;
    private LocalDateTime createdAt;

    // Constructor vacío
    public Role() {
    }

    // Constructor con nombre
    public Role(String name) {
        this.name = name;
    }

    // Constructor completo
    public Role(Long id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters y Setters
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return name != null && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}