package com.riwi.h1.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venue {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Integer maxCapacity;
    private String type;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
