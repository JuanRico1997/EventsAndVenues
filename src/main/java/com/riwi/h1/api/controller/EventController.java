package com.riwi.h1.api.controller;


import com.riwi.h1.api.dto.request.EventRequest;
import com.riwi.h1.api.dto.response.EventResponse;
import com.riwi.h1.application.service.EventService;
import com.riwi.h1.domain.entity.Event;
import com.riwi.h1.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de Eventos.
 * Expone endpoints para operaciones CRUD sobre eventos.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "API para la gestión de eventos de la tiquetera")
public class EventController {

    private final EventService eventService;

    /**
     * Crea un nuevo evento.
     *
     * @param request Datos del evento a crear
     * @return El evento creado con código 201 (CREATED)
     */
    @PostMapping
    @Operation(
            summary = "Crear un nuevo evento",
            description = "Crea un nuevo evento en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Evento creado exitosamente",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o validación fallida"
            )
    })
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request) {

        Event event = mapToEntity(request);
        Event createdEvent = eventService.create(event);
        EventResponse response = mapToResponse(createdEvent);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los eventos.
     *
     * @return Lista de todos los eventos con código 200 (OK)
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los eventos",
            description = "Retorna una lista de todos los eventos registrados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos obtenida exitosamente"
    )
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<Event> events = eventService.findAll();
        List<EventResponse> response = events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un evento por su ID.
     *
     * @param id ID del evento a buscar
     * @return El evento encontrado con código 200 (OK)
     * @throws ResourceNotFoundException si el evento no existe
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener evento por ID",
            description = "Retorna los detalles de un evento específico por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evento encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evento no encontrado"
            )
    })
    public ResponseEntity<EventResponse> getEventById(
            @Parameter(description = "ID del evento a buscar", required = true)
            @PathVariable Long id) {

        Event event = eventService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        EventResponse response = mapToResponse(event);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un evento existente.
     *
     * @param id ID del evento a actualizar
     * @param request Nuevos datos del evento
     * @return El evento actualizado con código 200 (OK)
     * @throws ResourceNotFoundException si el evento no existe
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un evento",
            description = "Actualiza los datos de un evento existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evento actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evento no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o validación fallida"
            )
    })
    public ResponseEntity<EventResponse> updateEvent(
            @Parameter(description = "ID del evento a actualizar", required = true)
            @PathVariable Long id,
            @Valid @RequestBody EventRequest request) {

        Event eventData = mapToEntity(request);
        Event updatedEvent = eventService.update(id, eventData);
        EventResponse response = mapToResponse(updatedEvent);

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un evento por su ID.
     *
     * @param id ID del evento a eliminar
     * @return Código 204 (NO_CONTENT) si se eliminó correctamente
     * @throws ResourceNotFoundException si el evento no existe
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un evento",
            description = "Elimina un evento del sistema por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Evento eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evento no encontrado"
            )
    })
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID del evento a eliminar", required = true)
            @PathVariable Long id) {

        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene todos los eventos de un venue específico.
     *
     * @param venueId ID del venue
     * @return Lista de eventos en ese venue con código 200 (OK)
     */
    @GetMapping("/venue/{venueId}")
    @Operation(
            summary = "Obtener eventos por venue",
            description = "Retorna todos los eventos asociados a un venue específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de eventos obtenida exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            )
    })
    public ResponseEntity<List<EventResponse>> getEventsByVenue(
            @Parameter(description = "ID del venue", required = true)
            @PathVariable Long venueId) {

        List<Event> events = eventService.findByVenueId(venueId);
        List<EventResponse> response = events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene solo los eventos activos.
     *
     * @return Lista de eventos activos con código 200 (OK)
     */
    @GetMapping("/active")
    @Operation(
            summary = "Obtener eventos activos",
            description = "Retorna todos los eventos que están activos en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos activos obtenida exitosamente"
    )
    public ResponseEntity<List<EventResponse>> getActiveEvents() {
        List<Event> events = eventService.findActiveEvents();
        List<EventResponse> response = events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene los eventos futuros (próximos).
     *
     * @return Lista de eventos futuros con código 200 (OK)
     */
    @GetMapping("/upcoming")
    @Operation(
            summary = "Obtener eventos próximos",
            description = "Retorna todos los eventos que aún no han ocurrido"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos próximos obtenida exitosamente"
    )
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {
        List<Event> events = eventService.findUpcomingEvents();
        List<EventResponse> response = events.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ========== 🆕 NUEVOS ENDPOINTS CON PAGINACIÓN ==========

    @GetMapping("/paginated")
    @Operation(
            summary = "Obtener eventos con paginación",
            description = "Retorna una página de eventos con soporte para paginación y ordenamiento"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de eventos obtenida exitosamente"
    )
    public ResponseEntity<Page<EventResponse>> getEventsPaginated(
            @Parameter(description = "Número de página (inicia en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo por el cual ordenar", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Dirección de ordenamiento (asc o desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Event> eventPage = eventService.findAllPaginated(pageable);
        Page<EventResponse> responsePage = eventPage.map(this::mapToResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/paginated/active")
    @Operation(
            summary = "Obtener eventos activos con paginación",
            description = "Retorna una página de eventos activos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de eventos activos obtenida exitosamente"
    )
    public ResponseEntity<Page<EventResponse>> getActiveEventsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Event> eventPage = eventService.findActiveEventsPaginated(pageable);
        Page<EventResponse> responsePage = eventPage.map(this::mapToResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/paginated/upcoming")
    @Operation(
            summary = "Obtener eventos próximos con paginación",
            description = "Retorna una página de eventos que aún no han ocurrido"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de eventos próximos obtenida exitosamente"
    )
    public ResponseEntity<Page<EventResponse>> getUpcomingEventsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Event> eventPage = eventService.findUpcomingEventsPaginated(pageable);
        Page<EventResponse> responsePage = eventPage.map(this::mapToResponse);

        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Buscar eventos con filtros y paginación",
            description = "Busca eventos aplicando filtros opcionales: venueId, active, startDate"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Página de eventos filtrados obtenida exitosamente"
    )
    public ResponseEntity<Page<EventResponse>> searchEvents(
            @Parameter(description = "ID del venue (opcional)")
            @RequestParam(required = false) Long venueId,

            @Parameter(description = "Estado activo (opcional)")
            @RequestParam(required = false) Boolean active,

            @Parameter(description = "Fecha de inicio (eventos posteriores a esta fecha) (opcional)",
                    example = "2025-12-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Event> eventPage = eventService.findWithFilters(venueId, active, startDate, pageable);
        Page<EventResponse> responsePage = eventPage.map(this::mapToResponse);

        return ResponseEntity.ok(responsePage);
    }



    // ========== MÉTODOS PRIVADOS DE MAPEO ==========

    /**
     * Mapea un EventRequest a una entidad Event.
     *
     * @param request DTO de entrada
     * @return Entidad Event
     */
    private Event mapToEntity(EventRequest request) {
        return Event.builder()
                .name(request.getName())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .venueId(request.getVenueId())
                .capacity(request.getCapacity())
                .ticketPrice(request.getTicketPrice())
                .active(request.getActive())
                .build();
    }

    /**
     * Mapea una entidad Event a un EventResponse.
     *
     * @param event Entidad Event
     * @return DTO de salida
     */
    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .venueId(event.getVenueId())
                .capacity(event.getCapacity())
                .ticketPrice(event.getTicketPrice())
                .active(event.getActive())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
