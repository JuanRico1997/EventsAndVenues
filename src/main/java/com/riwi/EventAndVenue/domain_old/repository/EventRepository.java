package com.riwi.EventAndVenue.domain_old.repository;

import com.riwi.EventAndVenue.domain_old.entity.Event;
import java.util.List;
import java.util.Optional;


public interface EventRepository {


    Event save(Event event);

    List<Event> findAll();

    Optional<Event> findById(Long id);

    Event update(Event event);

    boolean deleteById(Long id);

    boolean existsById(Long id);

    List<Event> findByVenueId(Long venueId);
}
