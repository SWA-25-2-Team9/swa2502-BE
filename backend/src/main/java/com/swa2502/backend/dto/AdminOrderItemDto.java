package com.swa2502.backend.dto;

import com.swa2502.backend.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminOrderItemDto {
    private Long orderItemId;
    private Long orderId;
    private String menuName;
    private Integer quantity;
    private Integer price;
    private List<Long> selectedOptionIds;
    private OrderStatus status;
}
