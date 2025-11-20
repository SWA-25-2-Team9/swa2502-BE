package com.swa2502.backend.service;

import com.swa2502.backend.dto.CartAddRequestDto;
import com.swa2502.backend.dto.CartAddResponseDto;
import com.swa2502.backend.dto.CartResponseDto;

public interface CartService {
    CartAddResponseDto addToCart(Long memberId, CartAddRequestDto dto);
    CartResponseDto getCart(Long memberId);
    CartResponseDto updateQuantity(Long memberId, Long cartItemId, int quantity);
    int deleteItem(Long memberId, Long cartItemId);
    String clearCart(Long memberId);
}
