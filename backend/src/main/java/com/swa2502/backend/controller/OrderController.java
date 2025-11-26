package com.swa2502.backend.controller;

import com.swa2502.backend.dto.CurrentOrderResponseDto;
import com.swa2502.backend.dto.OrderRequestDto;
import com.swa2502.backend.dto.OrderResponseDto;
import com.swa2502.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create Order", description = "Create a new order from cart items")
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequestDto request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(orderService.createOrder(memberId, request));
    }

    @GetMapping("/current")
    @Operation(summary = "Get Current Order", description = "Get the current active order")
    public ResponseEntity<CurrentOrderResponseDto> getCurrentOrder(@AuthenticationPrincipal UserDetails userDetails) {
        
        Long memberId = Long.parseLong(userDetails.getUsername());
        CurrentOrderResponseDto response = orderService.getCurrentOrder(memberId);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
