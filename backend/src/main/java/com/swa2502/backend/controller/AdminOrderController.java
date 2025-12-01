package com.swa2502.backend.controller;

import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.dto.AdminOrderItemDto;
import com.swa2502.backend.dto.ShopStatsDto;
import com.swa2502.backend.service.AdminOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Order", description = "Admin/매장관리 주문 API")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/shops/orders")
    @Operation(summary = "내 가게 주문 목록", description = "특정 가게의 모든 주문 항목을 조회합니다.")
    public ResponseEntity<List<AdminOrderItemDto>> getShopOrders(@RequestParam("shopId") Long shopId) {
        return ResponseEntity.ok(adminOrderService.getOrdersByShopId(shopId));
    }

    @GetMapping("/shops/orders/status")
    @Operation(summary = "OrderStatus별 주문", description = "특정 가게의 특정 상태의 주문 항목을 조회합니다.")
    public ResponseEntity<List<AdminOrderItemDto>> getShopOrdersByStatus(
            @RequestParam("shopId") Long shopId,
            @RequestParam("status") OrderStatus status) {
        return ResponseEntity.ok(adminOrderService.getOrdersByShopIdAndStatus(shopId, status));
    }

    @GetMapping("/shops/my/stats")
    @Operation(summary = "오늘 매출/통계", description = "오늘의 매출 및 주문 통계를 조회합니다.")
    public ResponseEntity<ShopStatsDto> getShopStats(@RequestParam("shopId") Long shopId) {
        return ResponseEntity.ok(adminOrderService.getShopStats(shopId));
    }

    @PatchMapping("/orders/{orderItemId}/next")
    @Operation(summary = "다음 상태 변경", description = "주문 항목의 상태를 다음 단계로 변경합니다.")
    public ResponseEntity<AdminOrderItemDto> updateStatusNext(@PathVariable("orderItemId") Long orderItemId) {
        return ResponseEntity.ok(adminOrderService.updateStatusNext(orderItemId));
    }

    @PatchMapping("/orders/{orderItemId}/prev")
    @Operation(summary = "이전 상태 변경", description = "주문 항목의 상태를 이전 단계로 변경합니다.")
    public ResponseEntity<AdminOrderItemDto> updateStatusPrev(@PathVariable("orderItemId") Long orderItemId) {
        return ResponseEntity.ok(adminOrderService.updateStatusPrev(orderItemId));
    }

    @PatchMapping("/orders/{orderItemId}/delete")
    @Operation(summary = "orderItem 취소", description = "주문 항목을 취소 처리합니다.")
    public ResponseEntity<AdminOrderItemDto> cancelOrderItem(@PathVariable("orderItemId") Long orderItemId) {
        return ResponseEntity.ok(adminOrderService.cancelOrderItem(orderItemId));
    }
}
