package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response;

import java.util.Set;

/**
 * DTO para respuesta de autenticación.
 *
 * Se usa tanto para login como para registro.
 */
public class AuthResponse {

    private Long userId;
    private String username;
    private String email;
    private String token;
    private String tokenType = "Bearer";
    private Set<String> roles;

    // Constructor vacío
    public AuthResponse() {
    }

    // Constructor para login (con token)
    public AuthResponse(String token, Long userId, String username, String email, Set<String> roles) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    // Constructor para registro (sin token inicialmente)
    public AuthResponse(Long userId, String username, String email, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    // Getters y Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}