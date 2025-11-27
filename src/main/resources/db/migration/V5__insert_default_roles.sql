-- V5__insert_default_roles.sql
-- Migración: Insertar roles por defecto del sistema

INSERT INTO roles (name, description) VALUES
('ROLE_USER', 'Usuario normal del sistema'),
('ROLE_ADMIN', 'Administrador con acceso total'),
('ROLE_ORGANIZER', 'Organizador de eventos');