package com.swa2502.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantOccupancyDto {
    private Long restaurantId;
    private String name;
    private Double occupancyRate;
    private Long occupiedSeats;
    private Integer totalSeats;
    private String level;
}
