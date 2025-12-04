package com.swa2502.backend.service;

import com.swa2502.backend.domain.Order;
import com.swa2502.backend.domain.OrderItem;
import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.domain.Restaurant;
import com.swa2502.backend.domain.Shop;
import com.swa2502.backend.dto.RestaurantCongestionDto;
import com.swa2502.backend.dto.RestaurantOccupancyDto;
import com.swa2502.backend.dto.RestaurantWithShopsDto;
import com.swa2502.backend.dto.ShopWithQueueDto;
import com.swa2502.backend.exception.CustomException;
import com.swa2502.backend.exception.ErrorCode;
import com.swa2502.backend.repository.OrderItemRepository;
import com.swa2502.backend.repository.RestaurantRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final OrderItemRepository orderItemRepository;

    // --- Congestion Tab Level 1: GET /api/v1/restaurants ---
    public List<RestaurantOccupancyDto> getAllRestaurantsOccupancy() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream()
                .map(this::calculateOccupancy)
                .collect(Collectors.toList());
    }

    private RestaurantOccupancyDto calculateOccupancy(Restaurant restaurant) {
        List<Shop> shops = restaurant.getShops();
        List<OrderStatus> relevantStatuses = List.of(OrderStatus.ACCEPTED, OrderStatus.READY, OrderStatus.PICKED_UP);
        List<OrderItem> orderItems = orderItemRepository.findByMenuItem_ShopInAndStatusIn(shops, relevantStatuses);

        Set<Order> uniqueOrders = orderItems.stream().map(OrderItem::getOrder).collect(Collectors.toSet());

        long occupiedSeats = 0;
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);

        for (Order order : uniqueOrders) {
            boolean isCookingOrReady = order.getOrderItems().stream()
                .anyMatch(item -> item.getStatus() == OrderStatus.ACCEPTED || item.getStatus() == OrderStatus.READY);

            boolean isPickedUp = order.getOrderItems().stream()
                .anyMatch(item -> item.getStatus() == OrderStatus.PICKED_UP);

            if (isCookingOrReady) {
                occupiedSeats++;
            } else if (isPickedUp) {
                if (order.getPickupAt() != null && order.getPickupAt().isAfter(fifteenMinutesAgo)) {
                    occupiedSeats++;
                }
            }
        }

        int totalSeats = 100; // Assuming a fixed total seats for now
        Double calculatedOccupancyRate = (totalSeats > 0) ? (double) occupiedSeats / totalSeats : 0.0;

        String level;
        if (calculatedOccupancyRate >= 1.0) {
            level = "full";
        } else if (calculatedOccupancyRate >= 0.7) {
            level = "high";
        } else if (calculatedOccupancyRate >= 0.4) {
            level = "medium";
        } else {
            level = "low";
        }

        return RestaurantOccupancyDto.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .occupancyRate(calculatedOccupancyRate)
                .occupiedSeats(occupiedSeats)
                .totalSeats(totalSeats)
                .level(level)
                .build();
    }

    // --- Home Tab: GET /api/v1/restaurants/with-shops ---
    public List<RestaurantWithShopsDto> getAllRestaurantsWithShops() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream().map(this::convertToRestaurantWithShopsDto).collect(Collectors.toList());
    }

    private RestaurantWithShopsDto convertToRestaurantWithShopsDto(Restaurant restaurant) {
        // Calculate occupancy rate for the restaurant
        RestaurantOccupancyDto occupancyDto = calculateOccupancy(restaurant);

        List<ShopWithQueueDto> shopDtos = restaurant.getShops().stream()
                .map(this::convertShopToShopWithQueueDto)
                .collect(Collectors.toList());

        return RestaurantWithShopsDto.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .occupancyRate(occupancyDto.getOccupancyRate()) // Use calculated occupancy
                .shops(shopDtos)
                .build();
    }

    private ShopWithQueueDto convertShopToShopWithQueueDto(Shop shop) {
        List<OrderStatus> waitingStatuses = List.of(OrderStatus.ACCEPTED);
        long waitingOrdersCount = orderItemRepository.countByMenuItem_ShopAndStatusIn(shop, waitingStatuses);

        int etaMinutes = (int) (waitingOrdersCount * 5);

        return ShopWithQueueDto.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .currentQueue((int) waitingOrdersCount) // Use the dynamic count here
                .etaMinutes(etaMinutes)
                .isOpen(shop.getIsOpen())
                .build();
    }

    // --- Congestion Tab Level 2: GET /api/v1/restaurants/{restaurantId}/shops/congestion ---
    public RestaurantCongestionDto getShopsWithCongestion(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

        // Calculate occupancy for the restaurant
        RestaurantOccupancyDto occupancyDto = calculateOccupancy(restaurant);

        // Get shop details with ETA
        List<ShopWithQueueDto> shopDtos = restaurant.getShops().stream()
                .map(this::convertShopToShopWithQueueDto)
                .collect(Collectors.toList());

        return RestaurantCongestionDto.builder()
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getName())
                .occupancyRate(occupancyDto.getOccupancyRate())
                .shops(shopDtos)
                .build();
    }

    // --- Helper method for ETA calculation ---
    public int calculateShopEta(Shop shop) {
        List<OrderStatus> waitingStatuses = List.of(OrderStatus.ACCEPTED);
        long waitingOrders = orderItemRepository.countByMenuItem_ShopAndStatusIn(shop, waitingStatuses);
        return (int) (waitingOrders * 5);
    }
}
