package com.swa2502.backend.controller;

import com.swa2502.backend.dto.RestaurantCongestionDto;
import com.swa2502.backend.dto.RestaurantOccupancyDto;
import com.swa2502.backend.dto.RestaurantWithShopsDto;
import com.swa2502.backend.dto.ShopWithQueueDto;
import com.swa2502.backend.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "식당 관련 API")
@PreAuthorize("hasRole('USER')")
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
    @Operation(summary = "식당 및 가게 목록 조회", description = "모든 식당과 해당 식당에 속한 가게 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RestaurantWithShopsDto.class)))
    })
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
