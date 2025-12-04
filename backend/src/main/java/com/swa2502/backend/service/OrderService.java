package com.swa2502.backend.service;

import com.swa2502.backend.dto.CurrentOrderResponseDto;
import com.swa2502.backend.dto.OrderRequestDto;
import com.swa2502.backend.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    List<OrderResponseDto> createOrder(Long memberId, OrderRequestDto request);
    List<CurrentOrderResponseDto> getCurrentOrder(Long memberId);
}
