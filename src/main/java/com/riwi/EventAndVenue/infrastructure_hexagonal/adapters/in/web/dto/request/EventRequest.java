package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO para recibir datos de Event desde el cliente.
 *
 * Contiene validaciones para asegurar que los datos sean correctos.
 * Se usa en los endpoints POST y PUT.
 */
public class EventRequest {

    @NotBlank(message = "El nombre del evento es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDateTime eventDate;

    private Long venueId;

    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;

    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    private Double ticketPrice;

    private Boolean active;

    // Constructor vacío
    public EventRequest() {
    }

    // Constructor completo
    public EventRequest(String name, String description, LocalDateTime eventDate,
                        Long venueId, Integer capacity, Double ticketPrice, Boolean active) {
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.active = active;
    }

    // GETTERS Y SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}