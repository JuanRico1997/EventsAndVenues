package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.model.User;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.RegisterUserUseCase;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.RoleEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.UserEntity;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.RoleJpaRepository;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementación del caso de uso RegisterUser.
 */
@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegisterUserUseCaseImpl.class);

    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCaseImpl(UserJpaRepository userRepository,
                                   RoleJpaRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public User execute(User user) {
        log.info("USER_REGISTER_START username={} email={}", user.getUsername(), user.getEmail());

        // REGLA 1: Validar que el username no exista
        if (userRepository.existsByUsername(user.getUsername())) {
            log.error("USER_REGISTER_FAILED username={} reason=UsernameExists", user.getUsername());
            throw new IllegalArgumentException("El username ya está en uso: " + user.getUsername());
        }

        // REGLA 2: Validar que el email no exista
        if (userRepository.existsByEmail(user.getEmail())) {
            log.error("USER_REGISTER_FAILED email={} reason=EmailExists", user.getEmail());
            throw new IllegalArgumentException("El email ya está en uso: " + user.getEmail());
        }

        // REGLA 3: Cifrar la contraseña
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        // REGLA 4: Asignar rol ROLE_USER por defecto
        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Rol ROLE_USER no encontrado en la base de datos"));

        // Crear entidad UserEntity
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(encodedPassword);
        userEntity.setEnabled(true);
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setUpdatedAt(LocalDateTime.now());

        // Asignar rol
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);
        userEntity.setRoles(roles);

        // Guardar en la base de datos
        UserEntity savedEntity = userRepository.save(userEntity);

        log.info("USER_REGISTER_SUCCESS userId={} username={}", savedEntity.getId(), savedEntity.getUsername());

        // Convertir a modelo de dominio
        User registeredUser = new User();
        registeredUser.setId(savedEntity.getId());
        registeredUser.setUsername(savedEntity.getUsername());
        registeredUser.setEmail(savedEntity.getEmail());
        registeredUser.setEnabled(savedEntity.getEnabled());

        Set<String> roleNames = new HashSet<>();
        roleNames.add("ROLE_USER");
        registeredUser.setRoles(roleNames);

        return registeredUser;
    }
}