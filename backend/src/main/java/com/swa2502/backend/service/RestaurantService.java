package com.swa2502.backend.service;

import com.swa2502.backend.domain.Restaurant;
import com.swa2502.backend.domain.Shop;
import com.swa2502.backend.dto.RestaurantWithShopsDto;
import com.swa2502.backend.dto.ShopWithQueueDto;
import com.swa2502.backend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<RestaurantWithShopsDto> getAllRestaurantsWithShops() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private RestaurantWithShopsDto convertToDto(Restaurant restaurant) {
        // For demonstration, setting dummy values for occupiedSeats and totalSeats
        // In a real application, these would come from a real-time source or be calculated based on other data
        restaurant.setOccupiedSeats(50); // Dummy value
        restaurant.setTotalSeats(100); // Dummy value

        Double calculatedOccupancyRate = 0.0;
        if (restaurant.getTotalSeats() != null && restaurant.getTotalSeats() > 0) {
            calculatedOccupancyRate = (double) restaurant.getOccupiedSeats() / restaurant.getTotalSeats();
        }

        List<ShopWithQueueDto> shopDtos = restaurant.getShops().stream()
                .map(this::convertShopToDto)
                .collect(Collectors.toList());

        return RestaurantWithShopsDto.builder()
                .restaurantId(restaurant.getId())
                .name(restaurant.getName())
                .occupancyRate(calculatedOccupancyRate)
                .shops(shopDtos)
                .build();
    }

    private ShopWithQueueDto convertShopToDto(Shop shop) {
        return ShopWithQueueDto.builder()
                .shopId(shop.getId())
                .name(shop.getName())
                .currentQueue(shop.getCurrentQueue())
                .etaMinutes(shop.getEtaMinutes())
                .isOpen(shop.getIsOpen())
                .build();
    }
}
