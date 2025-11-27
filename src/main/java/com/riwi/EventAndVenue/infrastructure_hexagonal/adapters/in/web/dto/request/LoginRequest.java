package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para login de usuario.
 */
public class LoginRequest {

    @NotBlank(message = "El username o email es obligatorio")
    private String usernameOrEmail;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    // Constructor vacío
    public LoginRequest() {
    }

    // Constructor completo
    public LoginRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    // Getters y Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}