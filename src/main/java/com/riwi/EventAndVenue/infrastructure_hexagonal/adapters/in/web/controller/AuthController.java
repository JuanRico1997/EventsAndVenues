package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.controller;

import com.riwi.EventAndVenue.domain_hexagonal.model.User;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.LoginUserUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.RegisterUserUseCase;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request.LoginRequest;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request.RegisterRequest;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response.AuthResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para autenticación.
 *
 * Endpoints públicos para registro y login.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    /**
     * POST /auth/register
     * Registrar un nuevo usuario.
     *
     * Endpoint público - no requiere autenticación.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        log.info("HTTP_REQUEST method=POST path=/auth/register username={} email={}",
                request.getUsername(), request.getEmail());

        // Convertir DTO a modelo de dominio
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Sin cifrar, se cifra en el caso de uso

        // Ejecutar caso de uso
        User registeredUser = registerUserUseCase.execute(user);

        // Convertir a DTO de respuesta
        AuthResponse response = new AuthResponse(
                registeredUser.getId(),
                registeredUser.getUsername(),
                registeredUser.getEmail(),
                registeredUser.getRoles()
        );

        log.info("HTTP_RESPONSE method=POST path=/auth/register status=201 userId={} username={}",
                response.getUserId(), response.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * POST /auth/login
     * Autenticar usuario y obtener token JWT.
     *
     * Endpoint público - no requiere autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        log.info("HTTP_REQUEST method=POST path=/auth/login usernameOrEmail={}",
                request.getUsernameOrEmail());

        // Ejecutar caso de uso (retorna token JWT)
        String token = loginUserUseCase.execute(request.getUsernameOrEmail(), request.getPassword());

        // Crear respuesta (por ahora sin userId, username, email - solo token)
        // En producción, podrías extraer esta info del token o retornarla del caso de uso
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTokenType("Bearer");

        log.info("HTTP_RESPONSE method=POST path=/auth/login status=200 tokenGenerated=true");

        return ResponseEntity.ok(response);
    }
}