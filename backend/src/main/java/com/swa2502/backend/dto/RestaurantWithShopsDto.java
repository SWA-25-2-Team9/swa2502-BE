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
public class RestaurantWithShopsDto {
    private Long restaurantId;
    private String name;
    private Double occupancyRate;
    private List<ShopWithQueueDto> shops;
}
