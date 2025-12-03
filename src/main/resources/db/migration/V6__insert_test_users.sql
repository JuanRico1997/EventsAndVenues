-- Insertar usuario ADMIN para testing
-- Credenciales: username=admin, password=password123
INSERT INTO users (username, email, password, created_at)
VALUES ('admin', 'admin@test.com', '$2a$10$FhSBnj5JqbzPIrOtlUeSBemBtPkbmkdZCa.qay5YzJSfyXqm1e2d.', CURRENT_TIMESTAMP);

-- Insertar usuario ORGANIZER para testing
-- Credenciales: username=organizer, password=password123
INSERT INTO users (username, email, password, created_at)
VALUES ('organizer', 'organizer@test.com', '$2a$10$FhSBnj5JqbzPIrOtlUeSBemBtPkbmkdZCa.qay5YzJSfyXqm1e2d.', CURRENT_TIMESTAMP);

-- Asignar rol ADMIN al usuario admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Asignar rol ORGANIZER al usuario organizer
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'organizer' AND r.name = 'ROLE_ORGANIZER';