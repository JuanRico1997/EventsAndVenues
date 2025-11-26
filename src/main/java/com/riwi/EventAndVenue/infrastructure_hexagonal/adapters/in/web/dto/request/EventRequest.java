package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.dto.request;

import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.FutureOrNull;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnCreate;
import com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.validation.groups.OnUpdate;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO de entrada para Event con validaciones avanzadas y grupos.
 */
public class EventRequest {

    @NotBlank(message = "{event.name.required}", groups = {OnCreate.class})
    @Size(min = 3, max = 100, message = "{event.name.size}", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Size(max = 500, message = "{event.description.size}", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(message = "{event.eventDate.required}", groups = {OnCreate.class})
    @FutureOrNull(message = "{event.eventDate.future}", groups = {OnCreate.class, OnUpdate.class})
    private LocalDateTime eventDate;

    @NotNull(message = "{event.venueId.required}", groups = {OnCreate.class})
    private Long venueId;

    @Positive(message = "{event.capacity.positive}", groups = {OnCreate.class, OnUpdate.class})
    private Integer capacity;

    @PositiveOrZero(message = "{event.ticketPrice.positive}", groups = {OnCreate.class, OnUpdate.class})
    private Double ticketPrice;

    private Boolean active;

    // Constructor vacío
    public EventRequest() {
    }

    // Constructor completo
    public EventRequest(String name, String description, LocalDateTime eventDate, Long venueId,
                        Integer capacity, Double ticketPrice, Boolean active) {
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.capacity = capacity;
        this.ticketPrice = ticketPrice;
        this.active = active;
    }

    // Getters y Setters
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