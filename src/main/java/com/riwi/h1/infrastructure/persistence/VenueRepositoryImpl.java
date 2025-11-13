package com.riwi.h1.infrastructure.persistence;

import com.riwi.h1.domain.entity.Venue;
import com.riwi.h1.domain.repository.VenueRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Repository
public class VenueRepositoryImpl implements VenueRepository {


    private final List<Venue> venues = new ArrayList<>();

    private final AtomicLong idGenerator = new AtomicLong(1);


    @Override
    public Venue save(Venue venue) {
        venue.setId(idGenerator.getAndIncrement());
        venue.setCreatedAt(LocalDateTime.now());
        venue.setUpdatedAt(LocalDateTime.now());

        // Por defecto, el venue está disponible
        if (venue.getAvailable() == null) {
            venue.setAvailable(true);
        }

        venues.add(venue);
        return venue;
    }

    @Override
    public List<Venue> findAll() {
        return new ArrayList<>(venues);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venues.stream()
                .filter(venue -> venue.getId().equals(id))
                .findFirst();
    }

    @Override
    public Venue update(Venue venue) {
        Optional<Venue> existingVenue = findById(venue.getId());

        if (existingVenue.isEmpty()) {
            throw new IllegalArgumentException("Venue with ID " + venue.getId() + " not found");
        }

        // Remover el venue antiguo
        venues.removeIf(v -> v.getId().equals(venue.getId()));

        // Mantener la fecha de creación original y actualizar la de modificación
        venue.setCreatedAt(existingVenue.get().getCreatedAt());
        venue.setUpdatedAt(LocalDateTime.now());

        // Agregar el venue actualizado
        venues.add(venue);

        return venue;
    }


    @Override
    public boolean deleteById(Long id) {
        return venues.removeIf(venue -> venue.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return venues.stream()
                .anyMatch(venue -> venue.getId().equals(id));
    }

    @Override
    public List<Venue> findByCity(String city) {
        return venues.stream()
                .filter(venue -> venue.getCity() != null &&
                        venue.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public List<Venue> findByAvailable(Boolean available) {
        return venues.stream()
                .filter(venue -> venue.getAvailable() != null &&
                        venue.getAvailable().equals(available))
                .collect(Collectors.toList());
    }
}
