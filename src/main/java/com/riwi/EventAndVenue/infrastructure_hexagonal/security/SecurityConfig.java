package com.riwi.EventAndVenue.infrastructure_hexagonal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de seguridad de Spring Security.
 *
 * Define:
 * - Endpoints públicos vs protegidos
 * - Filtros JWT
 * - CORS
 * - Modo stateless (sin sesiones)
 * - Password encoder (BCrypt)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Habilita @PreAuthorize, @PostAuthorize, etc.
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Configura el SecurityFilterChain principal.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF (no necesario en APIs stateless con JWT)
                .csrf(csrf -> csrf.disable())

                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configurar autorización de peticiones
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos (sin autenticación)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()

                        // Endpoints de consulta (GET) - públicos
                        .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/venues/**").permitAll()

                        // Endpoints de escritura (POST, PUT, DELETE) - requieren autenticación
                        .requestMatchers(HttpMethod.POST, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/venues/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/venues/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/venues/**").authenticated()

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )

                // Modo stateless (sin sesiones)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Agregar filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Configuración adicional para H2 Console (solo desarrollo)
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    /**
     * Proveedor de autenticación DAO.
     * Usa CustomUserDetailsService y BCryptPasswordEncoder.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * AuthenticationManager - necesario para el proceso de login.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Password Encoder - BCrypt para cifrar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de CORS.
     * Permite peticiones desde cualquier origen (para desarrollo).
     * En producción, especifica los orígenes permitidos.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Orígenes permitidos (en producción, especifica los dominios reales)
        configuration.setAllowedOrigins(List.of("*"));

        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos
        configuration.setAllowedHeaders(List.of("*"));

        // Permitir credenciales (cookies, headers de autenticación)
        configuration.setAllowCredentials(false); // false cuando se usa allowedOrigins("*")

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}