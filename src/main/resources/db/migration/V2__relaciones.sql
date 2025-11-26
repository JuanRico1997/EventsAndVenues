-- V2__relaciones.sql
-- Migración: Agregar relaciones, foreign keys e índices

-- Foreign key de events hacia venues
ALTER TABLE events
ADD CONSTRAINT fk_event_venue
FOREIGN KEY (venue_id)
REFERENCES venues(id)
ON DELETE CASCADE;

-- Índices para mejorar rendimiento de consultas

-- Índice en venue_id para búsquedas de eventos por venue
CREATE INDEX idx_event_venue_id ON events(venue_id);

-- Índice en event_date para búsquedas por fecha
CREATE INDEX idx_event_date ON events(event_date);

-- Índice en active para filtrar eventos/venues activos
CREATE INDEX idx_event_active ON events(active);
CREATE INDEX idx_venue_active ON venues(active);

-- Índice en location para búsquedas de venues por ubicación
CREATE INDEX idx_venue_location ON venues(location);

-- Índice compuesto para consultas comunes (venue activo + eventos activos)
CREATE INDEX idx_event_venue_active ON events(venue_id, active);