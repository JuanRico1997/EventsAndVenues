package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.repository;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para RoleEntity.
 */
@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * @param name Nombre del rol (ej: ROLE_USER, ROLE_ADMIN)
     * @return Optional con el rol si existe
     */
    Optional<RoleEntity> findByName(String name);

    /**
     * Verifica si existe un rol con el nombre dado.
     *
     * @param name Nombre del rol
     * @return true si existe, false si no
     */
    boolean existsByName(String name);
}