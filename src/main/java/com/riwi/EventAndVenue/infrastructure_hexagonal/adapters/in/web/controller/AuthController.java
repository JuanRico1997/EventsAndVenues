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
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private final Counter usersRegisteredCounter;
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;

    public AuthController(RegisterUserUseCase registerUserUseCase,
                          LoginUserUseCase loginUserUseCase,
                          @Qualifier("usersRegisteredCounter") Counter usersRegisteredCounter,
                          @Qualifier("loginSuccessCounter") Counter loginSuccessCounter,
                          @Qualifier("loginFailureCounter") Counter loginFailureCounter) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.usersRegisteredCounter = usersRegisteredCounter;
        this.loginSuccessCounter = loginSuccessCounter;
        this.loginFailureCounter = loginFailureCounter;
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

        // Incrementar contador de usuarios registrados
        usersRegisteredCounter.increment();

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

        try {
            // Ejecutar caso de uso (retorna token JWT)
            String token = loginUserUseCase.execute(request.getUsernameOrEmail(), request.getPassword());

            // Crear respuesta (por ahora sin userId, username, email - solo token)
            // En producción, podrías extraer esta info del token o retornarla del caso de uso
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            response.setTokenType("Bearer");

            // Incrementar contador de login exitoso
            loginSuccessCounter.increment();

            log.info("HTTP_RESPONSE method=POST path=/auth/login status=200 tokenGenerated=true");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Incrementar contador de login fallido
            loginFailureCounter.increment();

            log.error("HTTP_RESPONSE method=POST path=/auth/login status=401 reason=AuthenticationFailed");
            throw e;
        }
    }
}