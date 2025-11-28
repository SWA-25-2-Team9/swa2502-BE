package com.swa2502.backend.dto;

import com.swa2502.backend.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentOrderResponseDto {
    private Long orderId;
    private Integer orderNumber;
    private String shopName;
    private OrderStatus status;
    private Long myTurn;
    private Integer estimatedReadyMin;
    private Integer totalPrice;
    private LocalDateTime orderedAt;
    private List<OrderItemDto> items;
}
