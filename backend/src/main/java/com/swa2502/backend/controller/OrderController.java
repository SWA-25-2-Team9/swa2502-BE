package com.swa2502.backend.controller;

import com.swa2502.backend.dto.CurrentOrderResponseDto;
import com.swa2502.backend.dto.OrderRequestDto;
import com.swa2502.backend.dto.OrderResponseDto;
import com.swa2502.backend.service.OrderService;
import java.util.List;
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
    @Operation(summary = "Order 생성", description = "cart에 담겨진 cartItems로부터 Order를 생성합니다.")
    public ResponseEntity<List<OrderResponseDto>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequestDto request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(orderService.createOrder(memberId, request));
    }

    @GetMapping("/current")
    @Operation(summary = "Current Order 반환", description = "current order를 반환합니다.")
    public ResponseEntity<CurrentOrderResponseDto> getCurrentOrder(@AuthenticationPrincipal UserDetails userDetails) {
        
        Long memberId = Long.parseLong(userDetails.getUsername());
        CurrentOrderResponseDto response = orderService.getCurrentOrder(memberId);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
