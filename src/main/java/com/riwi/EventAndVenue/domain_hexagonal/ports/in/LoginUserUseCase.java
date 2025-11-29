package com.riwi.EventAndVenue.domain_hexagonal.ports.in;

/**
 * Caso de uso: Autenticar usuario y generar token JWT.
 *
 * Puerto de entrada (Driving Port).
 */
public interface LoginUserUseCase {

    /**
     * Autentica un usuario y genera un token JWT.
     *
     * @param usernameOrEmail Username o email del usuario
     * @param password Password sin cifrar
     * @return Token JWT si la autenticación es exitosa
     * @throws IllegalArgumentException Si las credenciales son incorrectas
     */
    String execute(String usernameOrEmail, String password);
}