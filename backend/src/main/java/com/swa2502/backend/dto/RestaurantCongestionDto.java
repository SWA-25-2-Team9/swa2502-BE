package com.swa2502.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantCongestionDto {
    private Long restaurantId;
    private String restaurantName;
    private Double occupancyRate;
    private List<ShopWithQueueDto> shops;
}