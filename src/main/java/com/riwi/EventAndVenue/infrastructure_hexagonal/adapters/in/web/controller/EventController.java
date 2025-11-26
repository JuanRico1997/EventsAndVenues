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
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.out.persistence.adapter.EventJpaAdapter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody EventRequest request) {
        // Convertir DTO a dominio
        Event event = mapper.toDomain(request);

        // Ejecutar caso de uso
        Event createdEvent = createEventUseCase.execute(event);

        // Convertir dominio a DTO de respuesta
        EventResponse response = mapper.toResponse(createdEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/events/{id}
     * Actualizar un evento existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id,
                                                     @Valid @RequestBody EventRequest request) {
        // Convertir DTO a dominio
        Event event = mapper.toDomain(request);

        // Ejecutar caso de uso
        Event updatedEvent = updateEventUseCase.execute(id, event);

        // Convertir dominio a DTO de respuesta
        EventResponse response = mapper.toResponse(updatedEvent);

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/events/{id}
     * Eliminar un evento.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        deleteEventUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/events/{id}
     * Obtener un evento por ID.
     */

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        Event event = eventQueryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con ID: " + id));
        EventResponse response = mapper.toResponse(event);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/events
     * Listar todos los eventos.
     */
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<Event> events = eventQueryService.findAll();
        List<EventResponse> responses = mapper.toResponseList(events);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/venue/{venueId}
     * Listar eventos de un venue específico.
     */
    @GetMapping("/venue/{venueId}")
    public ResponseEntity<List<EventResponse>> getEventsByVenue(@PathVariable Long venueId) {
        List<Event> events = eventQueryService.findByVenueId(venueId);
        List<EventResponse> responses = mapper.toResponseList(events);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/active
     * Listar eventos activos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<EventResponse>> getActiveEvents() {
        List<Event> events = eventQueryService.findActiveEvents();
        List<EventResponse> responses = mapper.toResponseList(events);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/upcoming
     * Listar eventos próximos (a partir de hoy).
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {
        List<Event> events = eventQueryService.findUpcomingEvents();
        List<EventResponse> responses = mapper.toResponseList(events);
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
        List<Event> events = eventQueryService.findByDateRange(startDate, endDate);
        List<EventResponse> responses = mapper.toResponseList(events);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/events/venue/{venueId}/count
     * Contar eventos de un venue.
     */
    @GetMapping("/venue/{venueId}/count")
    public ResponseEntity<Long> countEventsByVenue(@PathVariable Long venueId) {
        long count = eventQueryService.countByVenueId(venueId);
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

        // Llamar al método de búsqueda filtrada
        List<Event> events = searchEventsUseCase.execute(
                filter.getVenueId(),
                filter.getActive(),
                filter.getStartDate(),
                filter.getEndDate(),
                filter.getName(),
                filter.getMinCapacity(),
                filter.getMaxPrice()
        );

        // Convertir a DTOs de respuesta
        List<EventResponse> responses = mapper.toResponseList(events);

        return ResponseEntity.ok(responses);
    }
}