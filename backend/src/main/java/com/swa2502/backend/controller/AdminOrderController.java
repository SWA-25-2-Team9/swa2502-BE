package com.swa2502.backend.controller;

import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.dto.AdminOrderItemDto;
import com.swa2502.backend.dto.ShopStatsDto;
import com.swa2502.backend.service.AdminOrderService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Order", description = "Admin/매장관리 주문 API")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/shops/orders")
    @Operation(summary = "내 가게 주문 목록", description = "특정 가게의 모든 주문 항목을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminOrderItemDto.class)))
    })
    public ResponseEntity<List<AdminOrderItemDto>> getShopOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adminOrderService.getOrdersByShopId(getMemberId(userDetails)));
    }

    @GetMapping("/shops/orders/status")
    @Operation(summary = "OrderStatus별 주문", description = "특정 가게의 특정 상태의 주문 항목을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AdminOrderItemDto.class)))
    })
    public ResponseEntity<List<AdminOrderItemDto>> getShopOrdersByStatus(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(adminOrderService.getOrdersByShopIdAndStatus(getMemberId(userDetails), status));
    }

    @GetMapping("/shops/my/stats")
    @Operation(summary = "오늘 매출/통계", description = "오늘의 매출 및 주문 통계를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = ShopStatsDto.class)))
    })
    public ResponseEntity<ShopStatsDto> getShopStats(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(adminOrderService.getShopStats(getMemberId(userDetails)));
    }

    @PatchMapping("/orders/{orderItemId}/next")
    @Operation(summary = "다음 상태 변경", description = "주문 항목의 상태를 다음 단계로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = AdminOrderItemDto.class)))
    })
    public ResponseEntity<AdminOrderItemDto> updateStatusNext(@PathVariable("orderItemId") Long orderItemId) {
        return ResponseEntity.ok(adminOrderService.updateStatusNext(orderItemId));
    }

    @PatchMapping("/orders/{orderItemId}/prev")
    @Operation(summary = "이전 상태 변경", description = "주문 항목의 상태를 이전 단계로 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상태 변경 성공",
                    content = @Content(schema = @Schema(implementation = AdminOrderItemDto.class)))
    })
    public ResponseEntity<AdminOrderItemDto> updateStatusPrev(@PathVariable("orderItemId") Long orderItemId) {
        return ResponseEntity.ok(adminOrderService.updateStatusPrev(orderItemId));
    }

    @PatchMapping("/orders/{orderItemId}/cancel")
    @Operation(summary = "orderItem 취소", description = "주문 항목을 취소 처리합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "취소 성공",
                    content = @Content(schema = @Schema(implementation = AdminOrderItemDto.class)))
    })
    public ResponseEntity<AdminOrderItemDto> cancelOrderItem(@PathVariable("orderItemId") Long orderItemId) {
        return ResponseEntity.ok(adminOrderService.cancelOrderItem(orderItemId));
    }

    private Long getMemberId(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
    return memberId;
    }
}
