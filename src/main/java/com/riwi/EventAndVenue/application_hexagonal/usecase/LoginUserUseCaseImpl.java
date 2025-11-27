package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.LoginUserUseCase;
import com.riwi.EventAndVenue.infrastructure_hexagonal.security.CustomUserDetailsService;
import com.riwi.EventAndVenue.infrastructure_hexagonal.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación del caso de uso Login.
 */
@Service
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(LoginUserUseCaseImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public LoginUserUseCaseImpl(AuthenticationManager authenticationManager,
                                JwtTokenProvider jwtTokenProvider,
                                CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String execute(String usernameOrEmail, String password) {
        log.info("USER_LOGIN_START usernameOrEmail={}", usernameOrEmail);

        try {
            // PASO 1: Cargar el usuario por username o email
            UserDetails userDetails = userDetailsService.loadUserByUsernameOrEmail(usernameOrEmail);

            // PASO 2: Autenticar con AuthenticationManager
            // Esto verifica que el password sea correcto usando BCrypt
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDetails.getUsername(), // Usar el username real (no el email)
                            password,
                            userDetails.getAuthorities()
                    )
            );

            // PASO 3: Generar token JWT
            String token = jwtTokenProvider.generateToken(authentication);

            log.info("USER_LOGIN_SUCCESS username={}", userDetails.getUsername());

            return token;

        } catch (BadCredentialsException ex) {
            log.error("USER_LOGIN_FAILED usernameOrEmail={} reason=InvalidCredentials", usernameOrEmail);
            throw new IllegalArgumentException("Credenciales inválidas");
        } catch (AuthenticationException ex) {
            log.error("USER_LOGIN_FAILED usernameOrEmail={} reason={}", usernameOrEmail, ex.getMessage());
            throw new IllegalArgumentException("Error de autenticación: " + ex.getMessage());
        }
    }
}
