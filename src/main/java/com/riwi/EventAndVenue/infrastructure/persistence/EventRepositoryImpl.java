package com.riwi.EventAndVenue.infrastructure.persistence;

import com.riwi.EventAndVenue.domain_old.entity.Event;
import com.riwi.EventAndVenue.domain_old.repository.EventRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final List<Event> events = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Event save(Event event) {
        event.setId(idGenerator.getAndIncrement());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());

        //Por defecto, el evento se crea activo
        if(event.getId() == null) {
            event.setActive(true);
        }

        events.add(event);
        return event;
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<>(events);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst();
    }

    @Override
    public Event update(Event event) {
        Optional<Event> existingEvent = findById(event.getId());

        if(existingEvent.isEmpty()){
            throw new IllegalArgumentException("Event with ID " + event.getId() + "not found.");
        }

        //Remover el evento antiguo
        events.removeIf(e -> e.getId().equals(event.getId()));

        // Mantener la fecha de creación original y actualizar la de modificación
        event.setCreatedAt(existingEvent.get().getCreatedAt());
        event.setUpdatedAt(LocalDateTime.now());

        //Agregar el evento actualizado
        events.add(event);
        return event;
    }

    @Override
    public boolean deleteById(Long id) {
        return events.removeIf(event -> event.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return events.stream()
                .anyMatch(event -> event.getId().equals(id));
    }

    @Override
    public List<Event> findByVenueId(Long venueId) {
        return events.stream()
                .filter(event -> event.getVenueId() != null && event.getVenueId().equals(venueId))
                .collect(Collectors.toList());
    }
}
