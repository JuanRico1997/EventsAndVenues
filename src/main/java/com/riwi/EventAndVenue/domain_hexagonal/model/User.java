package com.riwi.EventAndVenue.domain_hexagonal.model;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Modelo de dominio para User (Usuario).
 *
 * Representa un usuario del sistema con autenticación y roles.
 * NO tiene dependencias de infraestructura (JPA, Spring Security, etc.).
 */
public class User {

    private Long id;
    private String username;
    private String email;
    private String password; // Cifrada
    private Boolean enabled;
    private Set<String> roles; // Solo nombres de roles (ROLE_ADMIN, ROLE_USER, etc.)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public User() {
    }

    // Constructor completo
    public User(Long id, String username, String email, String password, Boolean enabled,
                Set<String> roles, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
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