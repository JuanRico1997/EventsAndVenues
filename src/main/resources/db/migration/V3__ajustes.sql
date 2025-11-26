-- V3__ajustes.sql
-- Migración: Ajustes adicionales y optimizaciones

-- Este script está preparado para futuros ajustes
-- Por ahora, agregamos algunos comentarios en las tablas

-- Comentario en tabla venues
COMMENT ON TABLE venues IS 'Tabla de lugares/venues donde se realizan eventos';

-- Comentario en tabla events
COMMENT ON TABLE events IS 'Tabla de eventos programados en diferentes venues';

-- Si en el futuro necesitas agregar la tabla de categorías (ManyToMany):
-- Puedes crear un nuevo script V4__categorias.sql
-- Ejemplo:
-- CREATE TABLE categories (...);
-- CREATE TABLE event_categories (...);