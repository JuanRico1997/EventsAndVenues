package com.riwi.EventAndVenue.domain_old.repository;

import com.riwi.EventAndVenue.domain_old.entity.Venue;
import java.util.List;
import java.util.Optional;


public interface VenueRepository {


    Venue save(Venue venue);

    List<Venue> findAll();

    Optional<Venue> findById(Long id);

    Venue update(Venue venue);

    boolean deleteById(Long id);

    boolean existsById(Long id);

    List<Venue> findByCity(String city);

    List<Venue> findByAvailable(Boolean available);
}
