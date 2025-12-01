package com.swa2502.backend.controller;

import com.swa2502.backend.dto.RestaurantWithShopsDto;
import com.swa2502.backend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/with-shops")
    public ResponseEntity<List<RestaurantWithShopsDto>> getAllRestaurantsWithShops() {
        List<RestaurantWithShopsDto> restaurants = restaurantService.getAllRestaurantsWithShops();
        return ResponseEntity.ok(restaurants);
    }
}
