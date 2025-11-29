package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

import com.riwi.EventAndVenue.domain_hexagonal.model.User;

/**
 * Caso de uso: Registrar un nuevo usuario.
 *
 * Puerto de entrada (Driving Port).
 */
public interface RegisterUserUseCase {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user Usuario a registrar (password sin cifrar)
     * @return Usuario registrado con ID asignado
     */
    User execute(User user);
}