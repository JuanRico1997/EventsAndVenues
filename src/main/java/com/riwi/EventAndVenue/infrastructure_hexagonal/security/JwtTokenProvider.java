package com.riwi.EventAndVenue.infrastructure_hexagonal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Proveedor de tokens JWT.
 *
 * Responsabilidades:
 * - Generar tokens JWT
 * - Validar tokens JWT
 * - Extraer información de los tokens (username, roles)
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Genera un token JWT a partir de una autenticación de Spring Security.
     *
     * @param authentication Objeto Authentication de Spring Security
     * @return Token JWT como String
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // Extraer roles del usuario como lista
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.debug("JWT_GENERATE username={} roles={}", username, roles);

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)  // Ahora es una lista, no String
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    /**
     * Extrae el username del token JWT.
     *
     * @param token Token JWT
     * @return Username
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Extrae los roles del token JWT.
     *
     * @param token Token JWT
     * @return Lista de roles
     */
    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("roles", List.class);
    }

    /**
     * Valida si un token JWT es válido.
     *
     * @param token Token JWT
     * @return true si es válido, false si no
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("JWT_INVALID_SIGNATURE token={}", token.substring(0, 20));
        } catch (MalformedJwtException ex) {
            log.error("JWT_MALFORMED token={}", token.substring(0, 20));
        } catch (ExpiredJwtException ex) {
            log.error("JWT_EXPIRED token={}", token.substring(0, 20));
        } catch (UnsupportedJwtException ex) {
            log.error("JWT_UNSUPPORTED token={}", token.substring(0, 20));
        } catch (IllegalArgumentException ex) {
            log.error("JWT_EMPTY");
        }
        return false;
    }

    /**
     * Obtiene la clave secreta para firmar los tokens.
     *
     * @return SecretKey
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}