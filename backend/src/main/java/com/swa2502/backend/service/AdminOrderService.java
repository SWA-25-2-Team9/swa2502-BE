package com.swa2502.backend.service;

import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.dto.AdminOrderItemDto;
import com.swa2502.backend.dto.ShopStatsDto;

import java.util.List;

public interface AdminOrderService {
    List<AdminOrderItemDto> getOrdersByShopId(Long shopId);
    List<AdminOrderItemDto> getOrdersByShopIdAndStatus(Long shopId, OrderStatus status);
    ShopStatsDto getShopStats(Long shopId);
    AdminOrderItemDto updateStatusNext(Long orderItemId);
    AdminOrderItemDto updateStatusPrev(Long orderItemId);
    AdminOrderItemDto cancelOrderItem(Long orderItemId);
}
