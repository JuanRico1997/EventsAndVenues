package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para UserEntity.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca un usuario por username.
     * Carga los roles con EAGER para Spring Security.
     *
     * @param username Nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Busca un usuario por email.
     *
     * @param email Email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Verifica si existe un usuario con el username dado.
     *
     * @param username Nombre de usuario
     * @return true si existe, false si no
     */
    boolean existsByUsername(String username);

    /**
     * Verifica si existe un usuario con el email dado.
     *
     * @param email Email
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * Busca un usuario por username o email.
     * Útil para login (permitir login con username o email).
     *
     * @param username Username
     * @param email Email
     * @return Optional con el usuario si existe
     */
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username OR u.email = :email")
    Optional<UserEntity> findByUsernameOrEmail(@Param("username") String username,
                                               @Param("email") String email);
}