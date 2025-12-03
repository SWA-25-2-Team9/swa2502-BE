package com.swa2502.backend.controller;

import com.swa2502.backend.dto.RestaurantCongestionDto;
import com.swa2502.backend.dto.RestaurantOccupancyDto;
import com.swa2502.backend.dto.RestaurantWithShopsDto;
import com.swa2502.backend.dto.ShopWithQueueDto;
import com.swa2502.backend.service.RestaurantService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    // Congestion Tab Level 1: GET /api/v1/restaurants
    @GetMapping
    public ResponseEntity<List<RestaurantOccupancyDto>> getAllRestaurantsOccupancy() {
        List<RestaurantOccupancyDto> restaurants = restaurantService.getAllRestaurantsOccupancy();
        return ResponseEntity.ok(restaurants);
    }

    // Home Tab: GET /api/v1/restaurants/with-shops
    @GetMapping("/with-shops")
    public ResponseEntity<List<RestaurantWithShopsDto>> getAllRestaurantsWithShops() {
        List<RestaurantWithShopsDto> restaurants = restaurantService.getAllRestaurantsWithShops();
        return ResponseEntity.ok(restaurants);
    }

    // Congestion Tab Level 2: GET /api/v1/restaurants/{restaurantId}/shops/congestion
    @GetMapping("/{restaurantId}/shops/congestion")
    public ResponseEntity<RestaurantCongestionDto> getShopsWithCongestion(@PathVariable("restaurantId") Long restaurantId) {
        RestaurantCongestionDto shops = restaurantService.getShopsWithCongestion(restaurantId);
        return ResponseEntity.ok(shops);
    }
}
