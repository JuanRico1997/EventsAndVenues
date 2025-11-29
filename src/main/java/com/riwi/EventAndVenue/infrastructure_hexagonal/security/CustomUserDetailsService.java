package com.riwi.EventAndVenue.infrastructure_hexagonal.security;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.UserEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio personalizado para cargar detalles del usuario.
 *
 * Implementa UserDetailsService de Spring Security.
 * Se usa durante el proceso de autenticación.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserJpaRepository userRepository;

    public CustomUserDetailsService(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga un usuario por username.
     *
     * Spring Security llama a este método durante la autenticación.
     *
     * @param username Username del usuario
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("LOAD_USER_DETAILS username={}", username);

        // Buscar usuario en la base de datos
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("USER_NOT_FOUND username={}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        // Verificar que el usuario está habilitado
        if (!userEntity.getEnabled()) {
            log.error("USER_DISABLED username={}", username);
            throw new UsernameNotFoundException("Usuario deshabilitado: " + username);
        }

        // Convertir roles de RoleEntity a GrantedAuthority
        Set<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        log.debug("USER_LOADED username={} roles={}", username, authorities);

        // Crear UserDetails de Spring Security
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword()) // Ya está cifrada con BCrypt
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!userEntity.getEnabled())
                .build();
    }

    /**
     * Carga un usuario por username o email.
     *
     * Útil para permitir login con username o email.
     *
     * @param usernameOrEmail Username o email
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {

        log.debug("LOAD_USER_DETAILS usernameOrEmail={}", usernameOrEmail);

        // Buscar por username o email
        UserEntity userEntity = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> {
                    log.error("USER_NOT_FOUND usernameOrEmail={}", usernameOrEmail);
                    return new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail);
                });

        // Verificar que el usuario está habilitado
        if (!userEntity.getEnabled()) {
            log.error("USER_DISABLED username={}", userEntity.getUsername());
            throw new UsernameNotFoundException("Usuario deshabilitado: " + userEntity.getUsername());
        }

        // Convertir roles
        Set<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        log.debug("USER_LOADED username={} roles={}", userEntity.getUsername(), authorities);

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!userEntity.getEnabled())
                .build();
    }
}
