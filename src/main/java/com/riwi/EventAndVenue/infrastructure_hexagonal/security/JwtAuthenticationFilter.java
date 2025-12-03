package com.riwi.EventAndVenue.infrastructure_hexagonal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filtro de autenticación JWT.
 *
 * Intercepta cada petición HTTP:
 * 1. Extrae el token JWT del header Authorization
 * 2. Valida el token
 * 3. Extrae username y roles del token
 * 4. Establece la autenticación en SecurityContext
 *
 * Se ejecuta una vez por petición (OncePerRequestFilter).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. Extraer token del header Authorization
            String jwt = getJwtFromRequest(request);

            // 2. Si hay token y es válido, establecer autenticación
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {

                // Extraer username y roles del token
                String username = tokenProvider.getUsernameFromToken(jwt);
                List<String> roles = tokenProvider.getRolesFromToken(jwt);

                // Convertir roles de List<String> a List<SimpleGrantedAuthority>
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Crear objeto Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establecer autenticación en SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("JWT_AUTH_SUCCESS username={} roles={} authorities={}", username, roles, authorities);
            }

        } catch (Exception ex) {
            log.error("JWT_AUTH_FAILED error={}", ex.getMessage());
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del header Authorization.
     *
     * Formato esperado: "Bearer <token>"
     *
     * @param request HttpServletRequest
     * @return Token JWT o null si no existe
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Verificar que el header existe y tiene el formato correcto
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remover "Bearer " del inicio
        }

        return null;
    }
}