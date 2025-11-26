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
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<VenueResponse> createVenue(
            @Validated(OnCreate.class) @RequestBody VenueRequest request) {
        // Convertir DTO a dominio
        Venue venue = mapper.toDomain(request);

        // Ejecutar caso de uso
        Venue createdVenue = createVenueUseCase.execute(venue);

        // Convertir dominio a DTO de respuesta
        VenueResponse response = mapper.toResponse(createdVenue);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/venues/{id}
     * Actualizar un venue existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long id,
                                                     @Validated(OnUpdate.class) @RequestBody VenueRequest request) {
        // Convertir DTO a dominio
        Venue venue = mapper.toDomain(request);

        // Ejecutar caso de uso
        Venue updatedVenue = updateVenueUseCase.execute(id, venue);

        // Convertir dominio a DTO de respuesta
        VenueResponse response = mapper.toResponse(updatedVenue);

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/venues/{id}
     * Eliminar un venue.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        deleteVenueUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/venues/{id}
     * Obtener un venue por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VenueResponse> getVenueById(@PathVariable Long id) {
        Venue venue = venueQueryService.findById(id)
                .orElseThrow(() -> new RuntimeException("Venue no encontrado con ID: " + id));
        VenueResponse response = mapper.toResponse(venue);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/venues
     * Listar todos los venues.
     */
    @GetMapping
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        List<Venue> venues = venueQueryService.findAll();
        List<VenueResponse> responses = mapper.toResponseList(venues);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/venues/active
     * Listar venues activos.
     */
    @GetMapping("/active")
    public ResponseEntity<List<VenueResponse>> getActiveVenues() {
        List<Venue> venues = venueQueryService.findActiveVenues();
        List<VenueResponse> responses = mapper.toResponseList(venues);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/venues/location/{location}
     * Buscar venues por ubicación.
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<VenueResponse>> getVenuesByLocation(@PathVariable String location) {
        List<Venue> venues = venueQueryService.findByLocation(location);
        List<VenueResponse> responses = mapper.toResponseList(venues);
        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/venues/capacity?minCapacity=...
     * Buscar venues por capacidad mínima.
     */
    @GetMapping("/capacity")
    public ResponseEntity<List<VenueResponse>> getVenuesByMinimumCapacity(
            @RequestParam Integer minCapacity) {
        List<Venue> venues = venueQueryService.findByMinimumCapacity(minCapacity);
        List<VenueResponse> responses = mapper.toResponseList(venues);
        return ResponseEntity.ok(responses);
    }

    /**
     * POST /api/venues/filter
     * Buscar venues con filtros dinámicos usando Specifications.
     */
    @PostMapping("/filter")
    public ResponseEntity<List<VenueResponse>> filterVenues(@RequestBody VenueFilter filter) {
        // Usar el caso de uso (arquitectura hexagonal correcta)
        List<Venue> venues = searchVenuesUseCase.execute(
                filter.getLocation(),
                filter.getMinCapacity(),
                filter.getMaxCapacity(),
                filter.getActive(),
                filter.getName(),
                filter.getHasEvents()
        );

        // Convertir a DTOs de respuesta
        List<VenueResponse> responses = mapper.toResponseList(venues);

        return ResponseEntity.ok(responses);
    }
}
