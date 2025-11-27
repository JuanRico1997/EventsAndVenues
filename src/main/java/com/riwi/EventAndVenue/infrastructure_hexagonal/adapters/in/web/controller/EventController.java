package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.controller;

import com.riwi.EventAndVenue.application_hexagonal.service.EventQueryService;
import com.riwi.EventAndVenue.domain_hexagonal.model.Event;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.CreateEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.SearchEventsUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.UpdateEventUseCase;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.filter.EventFilter;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request.EventRequest;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response.EventResponse;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.mapper.EventRestMapper;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnCreate;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnUpdate;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.adapter.EventJpaAdapter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para Event.
 *
 * Este es un adaptador de entrada que expone los endpoints HTTP.
 * Usa los casos de uso y el servicio de consulta para ejecutar la lógica de negocio.
 * Usa el mapper REST para convertir entre DTOs y modelos de dominio.
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);
    private final CreateEventUseCase createEventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final DeleteEventUseCase deleteEventUseCase;
    private final EventQueryService eventQueryService;
    private final SearchEventsUseCase searchEventsUseCase;
    private final EventRestMapper mapper;


    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente los casos de uso, servicio y mapper.
     */
    public EventController(CreateEventUseCase createEventUseCase,
                           UpdateEventUseCase updateEventUseCase,
                           DeleteEventUseCase deleteEventUseCase,
                           EventQueryService eventQueryService,
                           SearchEventsUseCase searchEventsUseCase,
                           EventRestMapper mapper) {
        this.createEventUseCase = createEventUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.deleteEventUseCase = deleteEventUseCase;
        this.eventQueryService = eventQueryService;
        this.searchEventsUseCase = searchEventsUseCase;
        this.mapper = mapper;
    }

    /**
     * POST /api/events
     * Crear un nuevo evento.
     */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @Validated(OnCreate.class) @RequestBody EventRequest request) {

        log.info("HTTP_REQUEST method=POST path=/api/events eventName={}", request.getName());

        Event event = mapper.toDomain(request);
        Event created = createEventUseCase.execute(event);
        EventResponse response = mapper.toResponse(created);

        log.info("HTTP_RESPONSE method=POST path=/api/events status=201 eventId={} eventName={}",
                response.getId(), response.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/events/{id}
     * Actualizar un evento existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id,
                                                     @Validated(OnUpdate.class) @RequestBody EventRequest request) {

        log.info("HTTP_REQUEST method=PUT path=/api/events/{} eventId={} eventName={}",
                id, id, request.getName());

        Event event = mapper.toDomain(request);
        Event updated = updateEventUseCase.execute(id, event);
        EventResponse response = mapper.toResponse(updated);

        log.info("HTTP_RESPONSE method=PUT path=/api/events/{} status=200 eventId={} eventName={}",
                id, response.getId(), response.getName());

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/events/{id}
     * Eliminar un evento.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {

        log.info("HTTP_REQUEST method=DELETE path=/api/events/{} eventId={}", id, id);

        deleteEventUseCase.execute(id);

        log.info("HTTP_RESPONSE method=DELETE path=/api/events/{} status=204 eventId={}", id, id);

        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/events/{id}
     * Obtener un evento por ID.
     */

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {

        log.info("HTTP_REQUEST method=GET path=/api/events/{} eventId={}", id, id);

        Event event = eventQueryService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        EventResponse response = mapper.toResponse(event);

        log.info("HTTP_RESPONSE method=GET path=/api/events/{} status=200 eventId={} eventName={}",
                id, response.getId(), response.getName());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/events
     * Listar todos los eventos.
     */
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {

        log.info("HTTP_REQUEST method=GET path=/api/events");

        List<Event> events = eventQueryService.findAll();
        List<EventResponse> responses = mapper.toResponseList(events);

        log.info("HTTP_RESPONSE method=GET path=/api/events status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/venue/{venueId}
     * Listar eventos de un venue específico.
     */
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventResponse>> getEventsByVenue(@PathVariable Long venueId) {

        log.info("HTTP_REQUEST method=GET path=/api/events/venue/{} venueId={}", venueId, venueId);

        List<Event> events = eventQueryService.findByVenueId(venueId);
        List<EventResponse> responses = mapper.toResponseList(events);

        log.info("HTTP_RESPONSE method=GET path=/api/events/venue/{} status=200 count={}", venueId, responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/active
     * Listar eventos activos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<EventResponse>> getActiveEvents() {

        log.info("HTTP_REQUEST method=GET path=/api/events/active");

        List<Event> events = eventQueryService.findActiveEvents();
        List<EventResponse> responses = mapper.toResponseList(events);

        log.info("HTTP_RESPONSE method=GET path=/api/events/active status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/upcoming
     * Listar eventos próximos (a partir de hoy).
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {

        log.info("HTTP_REQUEST method=GET path=/api/events/upcoming");

        List<Event> events = eventQueryService.findUpcomingEvents();
        List<EventResponse> responses = mapper.toResponseList(events);

        log.info("HTTP_RESPONSE method=GET path=/api/events/upcoming status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/search?startDate=...&endDate=...
     * Buscar eventos por rango de fechas.
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventResponse>> getEventsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {

        log.info("HTTP_REQUEST method=GET path=/api/events/search startDate={} endDate={}", startDate, endDate);

        List<Event> events = eventQueryService.findByDateRange(startDate, endDate);
        List<EventResponse> responses = mapper.toResponseList(events);

        log.info("HTTP_RESPONSE method=GET path=/api/events/search status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/venue/{venueId}/count
     * Contar eventos de un venue.
     */

    @GetMapping("/venue/{venueId}/count")
    public ResponseEntity<Long> countEventsByVenue(@PathVariable Long venueId) {

        log.info("HTTP_REQUEST method=GET path=/api/events/venue/{}/count venueId={}", venueId, venueId);

        long count = eventQueryService.countByVenueId(venueId);

        log.info("HTTP_RESPONSE method=GET path=/api/events/venue/{}/count status=200 count={}", venueId, count);

        return ResponseEntity.ok(count);
    }

    /**
     * POST /api/events/filter
     * Buscar eventos con filtros dinámicos.
     *
     * Usa Specifications para construir consultas dinámicas según los filtros proporcionados.
     */
    @PostMapping("/filter")
    public ResponseEntity<List<EventResponse>> filterEvents(@RequestBody EventFilter filter) {

        log.info("HTTP_REQUEST method=POST path=/api/events/filter venueId={} active={} name={}",
                filter.getVenueId(), filter.getActive(), filter.getName());

        List<Event> events = searchEventsUseCase.execute(
                filter.getVenueId(),
                filter.getActive(),
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getName(),
                filter.getMinCapacity(),
                filter.getMaxPrice()
        );

        List<EventResponse> responses = mapper.toResponseList(events);

        log.info("HTTP_RESPONSE method=POST path=/api/events/filter status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }
}