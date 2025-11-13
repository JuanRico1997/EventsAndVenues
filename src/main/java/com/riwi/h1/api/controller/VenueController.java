package com.riwi.h1.api.controller;


import com.riwi.h1.api.dto.request.VenueRequest;
import com.riwi.h1.api.dto.response.VenueResponse;
import com.riwi.h1.application.service.VenueService;
import com.riwi.h1.domain.entity.Venue;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de Venues (Lugares/Recintos).
 * Expone endpoints para operaciones CRUD sobre venues.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venues", description = "API para la gestión de venues (lugares/recintos) de la tiquetera")
public class VenueController {

    private final VenueService venueService;

    /**
     * Crea un nuevo venue.
     *
     * @param request Datos del venue a crear
     * @return El venue creado con código 201 (CREATED)
     */
    @PostMapping
    @Operation(
            summary = "Crear un nuevo venue",
            description = "Crea un nuevo venue (lugar/recinto) en el sistema con los datos proporcionados"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Venue creado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o validación fallida"
            )
    })
    public ResponseEntity<VenueResponse> createVenue(
            @Valid @RequestBody VenueRequest request) {

        Venue venue = mapToEntity(request);
        Venue createdVenue = venueService.create(venue);
        VenueResponse response = mapToResponse(createdVenue);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene todos los venues.
     *
     * @return Lista de todos los venues con código 200 (OK)
     */
    @GetMapping
    @Operation(
            summary = "Obtener todos los venues",
            description = "Retorna una lista de todos los venues registrados en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de venues obtenida exitosamente"
    )
    public ResponseEntity<List<VenueResponse>> getAllVenues() {
        List<Venue> venues = venueService.findAll();
        List<VenueResponse> response = venues.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un venue por su ID.
     *
     * @param id ID del venue a buscar
     * @return El venue encontrado con código 200 (OK)
     * @throws ResourceNotFoundException si el venue no existe
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener venue por ID",
            description = "Retorna los detalles de un venue específico por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venue encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            )
    })
    public ResponseEntity<VenueResponse> getVenueById(
            @Parameter(description = "ID del venue a buscar", required = true)
            @PathVariable Long id) {

        Venue venue = venueService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", id));

        VenueResponse response = mapToResponse(venue);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un venue existente.
     *
     * @param id ID del venue a actualizar
     * @param request Nuevos datos del venue
     * @return El venue actualizado con código 200 (OK)
     * @throws ResourceNotFoundException si el venue no existe
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un venue",
            description = "Actualiza los datos de un venue existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venue actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o validación fallida"
            )
    })
    public ResponseEntity<VenueResponse> updateVenue(
            @Parameter(description = "ID del venue a actualizar", required = true)
            @PathVariable Long id,
            @Valid @RequestBody VenueRequest request) {

        Venue venueData = mapToEntity(request);
        Venue updatedVenue = venueService.update(id, venueData);
        VenueResponse response = mapToResponse(updatedVenue);

        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un venue por su ID.
     *
     * @param id ID del venue a eliminar
     * @return Código 204 (NO_CONTENT) si se eliminó correctamente
     * @throws ResourceNotFoundException si el venue no existe
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un venue",
            description = "Elimina un venue del sistema por su ID. No se puede eliminar si tiene eventos asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Venue eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No se puede eliminar porque tiene eventos asociados"
            )
    })
    public ResponseEntity<Void> deleteVenue(
            @Parameter(description = "ID del venue a eliminar", required = true)
            @PathVariable Long id) {

        venueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene venues por ciudad.
     *
     * @param city Ciudad a buscar
     * @return Lista de venues en esa ciudad con código 200 (OK)
     */
    @GetMapping("/city/{city}")
    @Operation(
            summary = "Obtener venues por ciudad",
            description = "Retorna todos los venues ubicados en una ciudad específica"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de venues obtenida exitosamente"
    )
    public ResponseEntity<List<VenueResponse>> getVenuesByCity(
            @Parameter(description = "Nombre de la ciudad", required = true)
            @PathVariable String city) {

        List<Venue> venues = venueService.findByCity(city);
        List<VenueResponse> response = venues.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene solo los venues disponibles.
     *
     * @return Lista de venues disponibles con código 200 (OK)
     */
    @GetMapping("/available")
    @Operation(
            summary = "Obtener venues disponibles",
            description = "Retorna todos los venues que están disponibles para eventos"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de venues disponibles obtenida exitosamente"
    )
    public ResponseEntity<List<VenueResponse>> getAvailableVenues() {
        List<Venue> venues = venueService.findAvailableVenues();
        List<VenueResponse> response = venues.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la cantidad de eventos de un venue.
     *
     * @param id ID del venue
     * @return Cantidad de eventos asociados con código 200 (OK)
     */
    @GetMapping("/{id}/events/count")
    @Operation(
            summary = "Contar eventos de un venue",
            description = "Retorna la cantidad de eventos asociados a un venue específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Conteo obtenido exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            )
    })
    public ResponseEntity<Long> countEventsByVenue(
            @Parameter(description = "ID del venue", required = true)
            @PathVariable Long id) {

        long count = venueService.countEventsByVenue(id);
        return ResponseEntity.ok(count);
    }

    /**
     * Marca un venue como no disponible.
     *
     * @param id ID del venue
     * @return El venue actualizado con código 200 (OK)
     */
    @PatchMapping("/{id}/unavailable")
    @Operation(
            summary = "Marcar venue como no disponible",
            description = "Cambia el estado de un venue a no disponible"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venue actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            )
    })
    public ResponseEntity<VenueResponse> markAsUnavailable(
            @Parameter(description = "ID del venue", required = true)
            @PathVariable Long id) {

        Venue venue = venueService.markAsUnavailable(id);
        VenueResponse response = mapToResponse(venue);
        return ResponseEntity.ok(response);
    }

    /**
     * Marca un venue como disponible.
     *
     * @param id ID del venue
     * @return El venue actualizado con código 200 (OK)
     */
    @PatchMapping("/{id}/available")
    @Operation(
            summary = "Marcar venue como disponible",
            description = "Cambia el estado de un venue a disponible"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Venue actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Venue no encontrado"
            )
    })
    public ResponseEntity<VenueResponse> markAsAvailable(
            @Parameter(description = "ID del venue", required = true)
            @PathVariable Long id) {

        Venue venue = venueService.markAsAvailable(id);
        VenueResponse response = mapToResponse(venue);
        return ResponseEntity.ok(response);
    }

    // ========== MÉTODOS PRIVADOS DE MAPEO ==========

    /**
     * Mapea un VenueRequest a una entidad Venue.
     *
     * @param request DTO de entrada
     * @return Entidad Venue
     */
    private Venue mapToEntity(VenueRequest request) {
        return Venue.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .maxCapacity(request.getMaxCapacity())
                .type(request.getType())
                .available(request.getAvailable())
                .build();
    }

    /**
     * Mapea una entidad Venue a un VenueResponse.
     *
     * @param venue Entidad Venue
     * @return DTO de salida
     */
    private VenueResponse mapToResponse(Venue venue) {
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .country(venue.getCountry())
                .maxCapacity(venue.getMaxCapacity())
                .type(venue.getType())
                .available(venue.getAvailable())
                .createdAt(venue.getCreatedAt())
                .updatedAt(venue.getUpdatedAt())
                .build();
    }
}
