package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.controller;

import com.riwi.EventAndVenue.application_hexagonal.service.VenueQueryService;
import com.riwi.EventAndVenue.domain_hexagonal.model.Venue;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.CreateVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteVenueUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.SearchVenuesUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.UpdateVenueUseCase;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.filter.VenueFilter;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request.VenueRequest;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.response.VenueResponse;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.mapper.VenueRestMapper;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnCreate;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnUpdate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Venue.
 *
 * Este es un adaptador de entrada que expone los endpoints HTTP.
 * Usa los casos de uso y el servicio de consulta para ejecutar la lógica de negocio.
 * Usa el mapper REST para convertir entre DTOs y modelos de dominio.
 */
@RestController
@RequestMapping("/api/venues")
public class VenueController {

    private static final Logger log = LoggerFactory.getLogger(VenueController.class);
    private final CreateVenueUseCase createVenueUseCase;
    private final UpdateVenueUseCase updateVenueUseCase;
    private final DeleteVenueUseCase deleteVenueUseCase;
    private final VenueQueryService venueQueryService;
    private final SearchVenuesUseCase searchVenuesUseCase;
    private final VenueRestMapper mapper;

    /**
     * Constructor con inyección de dependencias.
     * Spring inyecta automáticamente los casos de uso, servicio y mapper.
     */
    public VenueController(CreateVenueUseCase createVenueUseCase,
                           UpdateVenueUseCase updateVenueUseCase,
                           DeleteVenueUseCase deleteVenueUseCase,
                           VenueQueryService venueQueryService,
                           SearchVenuesUseCase searchVenuesUseCase,
                           VenueRestMapper mapper) {
        this.createVenueUseCase = createVenueUseCase;
        this.updateVenueUseCase = updateVenueUseCase;
        this.deleteVenueUseCase = deleteVenueUseCase;
        this.venueQueryService = venueQueryService;
        this.searchVenuesUseCase = searchVenuesUseCase;
        this.mapper = mapper;
    }

    /**
     * POST /api/venues
     * Crear un nuevo venue.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<VenueResponse> createVenue(
            @Validated(OnCreate.class) @RequestBody VenueRequest request) {

        log.info("HTTP_REQUEST method=POST path=/api/venues venueName={}", request.getName());

        Venue venue = mapper.toDomain(request);
        Venue created = createVenueUseCase.execute(venue);
        VenueResponse response = mapper.toResponse(created);

        log.info("HTTP_RESPONSE method=POST path=/api/venues status=201 venueId={} venueName={}",
                response.getId(), response.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/venues/{id}
     * Actualizar un venue existente.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long id,
                                                     @Validated(OnUpdate.class) @RequestBody VenueRequest request) {

        log.info("HTTP_REQUEST method=PUT path=/api/venues/{} venueId={} venueName={}",
                id, id, request.getName());

        Venue venue = mapper.toDomain(request);
        Venue updated = updateVenueUseCase.execute(id, venue);
        VenueResponse response = mapper.toResponse(updated);

        log.info("HTTP_RESPONSE method=PUT path=/api/venues/{} status=200 venueId={} venueName={}",
                id, response.getId(), response.getName());

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/venues/{id}
     * Eliminar un venue.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZER')")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {

        log.info("HTTP_REQUEST method=DELETE path=/api/venues/{} venueId={}", id, id);

        deleteVenueUseCase.execute(id);

        log.info("HTTP_RESPONSE method=DELETE path=/api/venues/{} status=204 venueId={}", id, id);

        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/venues/{id}
     * Obtener un venue por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {

        log.info("HTTP_REQUEST method=GET path=/api/venues/{} venueId={}", id, id);

        Venue venue = venueQueryService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venue not found with id: " + id));

        VenueResponse response = mapper.toResponse(venue);

        log.info("HTTP_RESPONSE method=GET path=/api/venues/{} status=200 venueId={} venueName={}",
                id, response.getId(), response.getName());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/venues
     * Listar todos los venues.
     */
    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAllVenues() {

        log.info("HTTP_REQUEST method=GET path=/api/venues");

        List<Venue> venues = venueQueryService.findAll();
        List<VenueResponse> responses = mapper.toResponseList(venues);

        log.info("HTTP_RESPONSE method=GET path=/api/venues status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/venues/active
     * Listar venues activos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<VenueResponse>> getActiveVenues() {

        log.info("HTTP_REQUEST method=GET path=/api/venues/active");

        List<Venue> venues = venueQueryService.findActiveVenues();
        List<VenueResponse> responses = mapper.toResponseList(venues);

        log.info("HTTP_RESPONSE method=GET path=/api/venues/active status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/venues/location/{location}
     * Buscar venues por ubicación.
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<VenueResponse>> getVenuesByLocation(@PathVariable String location) {

        log.info("HTTP_REQUEST method=GET path=/api/venues/location/{} location={}", location, location);

        List<Venue> venues = venueQueryService.findByLocation(location);
        List<VenueResponse> responses = mapper.toResponseList(venues);

        log.info("HTTP_RESPONSE method=GET path=/api/venues/location/{} status=200 count={}", location, responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/venues/capacity?minCapacity=...
     * Buscar venues por capacidad mínima.
     */
    @GetMapping("/capacity")
    public ResponseEntity<List<VenueResponse>> getVenuesByMinimumCapacity(
            @RequestParam Integer minCapacity) {

        log.info("HTTP_REQUEST method=GET path=/api/venues/capacity minCapacity={}", minCapacity);

        List<Venue> venues = venueQueryService.findByMinimumCapacity(minCapacity);
        List<VenueResponse> responses = mapper.toResponseList(venues);

        log.info("HTTP_RESPONSE method=GET path=/api/venues/capacity status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }

    /**
     * POST /api/venues/filter
     * Buscar venues con filtros dinámicos usando Specifications.
     */
    @PostMapping("/filter")
    public ResponseEntity<List<VenueResponse>> filterVenues(@RequestBody VenueFilter filter) {

        log.info("HTTP_REQUEST method=POST path=/api/venues/filter location={} active={} name={}",
                filter.getLocation(), filter.getActive(), filter.getName());

        List<Venue> venues = searchVenuesUseCase.execute(
                filter.getLocation(),
                filter.getMinCapacity(),
                filter.getMaxCapacity(),
                filter.getActive(),
                filter.getName(),
                filter.getHasEvents()
        );

        List<VenueResponse> responses = mapper.toResponseList(venues);

        log.info("HTTP_RESPONSE method=POST path=/api/venues/filter status=200 count={}", responses.size());

        return ResponseEntity.ok(responses);
    }
}
