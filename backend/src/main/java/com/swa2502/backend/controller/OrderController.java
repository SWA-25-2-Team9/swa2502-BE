package com.swa2502.backend.controller;

import com.swa2502.backend.dto.CurrentOrderResponseDto;
import com.swa2502.backend.dto.OrderRequestDto;
import com.swa2502.backend.dto.OrderResponseDto;
import com.swa2502.backend.service.OrderService;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "주문 관련 API")
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Order 생성", description = "cart에 담겨진 cartItems로부터 Order를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 생성 성공",
                    content = @Content(schema = @Schema(implementation = OrderResponseDto.class)))
    })
    public ResponseEntity<List<OrderResponseDto>> createOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody OrderRequestDto request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(orderService.createOrder(memberId, request));
    }

    @GetMapping("/current")
    @Operation(summary = "Current Order 반환", description = "current order를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "현재 주문 조회 성공",
                    content = @Content(schema = @Schema(implementation = CurrentOrderResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "현재 주문 없음")
    })
    public ResponseEntity<CurrentOrderResponseDto> getCurrentOrder(@AuthenticationPrincipal UserDetails userDetails) {
        
        Long memberId = Long.parseLong(userDetails.getUsername());
        CurrentOrderResponseDto response = orderService.getCurrentOrder(memberId);
        if (response == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }
}
