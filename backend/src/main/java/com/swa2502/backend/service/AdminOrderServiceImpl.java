package com.swa2502.backend.service;

import com.swa2502.backend.domain.OrderItem;
import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.dto.AdminOrderItemDto;
import com.swa2502.backend.dto.ShopStatsDto;
import com.swa2502.backend.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderItemDto> getOrdersByShopId(Long shopId) {
        return orderItemRepository.findAllByMenuItem_Shop_Id(shopId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminOrderItemDto> getOrdersByShopIdAndStatus(Long shopId, OrderStatus status) {
        return orderItemRepository.findAllByMenuItem_Shop_IdAndStatus(shopId, status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ShopStatsDto getShopStats(Long shopId) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Long todaySales = orderItemRepository.sumTotalPriceByShopIdAndDate(shopId, startOfDay, endOfDay);
        Long orderCount = orderItemRepository.countOrdersByShopIdAndDate(shopId, startOfDay, endOfDay);

        // avgPrepTime는 임의로 0으로 표시
        Long avgPrepTime = 0L; 
        Long batchOrderCount = 0L;

        return ShopStatsDto.builder()
                .todaySales(todaySales != null ? todaySales : 0L)
                .orderCount(orderCount != null ? orderCount : 0L)
                .avgPrepTime(avgPrepTime)
                .batchOrderCount(batchOrderCount)
                .build();
    }

    @Override
    public AdminOrderItemDto updateStatusNext(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));
        
        OrderStatus nextStatus = getNextStatus(orderItem.getStatus());
        if (nextStatus != null) {
            orderItem.setStatus(nextStatus);
        }
        
        return convertToDto(orderItem);
    }

    @Override
    public AdminOrderItemDto updateStatusPrev(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));

        OrderStatus prevStatus = getPrevStatus(orderItem.getStatus());
        if (prevStatus != null) {
            orderItem.setStatus(prevStatus);
        }

        return convertToDto(orderItem);
    }

    @Override
    public AdminOrderItemDto cancelOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found"));
        
        orderItem.setStatus(OrderStatus.CANCELED);
        
        return convertToDto(orderItem);
    }

    private AdminOrderItemDto convertToDto(OrderItem orderItem) {
        return AdminOrderItemDto.builder()
                .orderItemId(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .menuName(orderItem.getMenuItem().getName())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .selectedOptionIds(orderItem.getSelectedOptionIds())
                .status(orderItem.getStatus())
                .build();
    }

    private OrderStatus getNextStatus(OrderStatus current) {
        // PENDING -> ACCEPTED -> COOKING -> READY -> PICKED_UP
        switch (current) {
            case PENDING: return OrderStatus.ACCEPTED;
            case ACCEPTED: return OrderStatus.COOKING;
            case COOKING: return OrderStatus.READY;
            case READY: return OrderStatus.PICKED_UP;
            default: return null;
        }
    }

    private OrderStatus getPrevStatus(OrderStatus current) {
        // PICKED_UP -> READY -> COOKING -> ACCEPTED -> PENDING
        switch (current) {
            case PICKED_UP: return OrderStatus.READY;
            case READY: return OrderStatus.COOKING;
            case COOKING: return OrderStatus.ACCEPTED;
            case ACCEPTED: return OrderStatus.PENDING;
            default: return null;
        }
    }
}
