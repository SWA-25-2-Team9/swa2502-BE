package com.swa2502.backend.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    private int cartCount;
    private int totalPrice;
    private List<CartItemResponseDto> items;

    static public CartResponseDto toDto(List<CartItemResponseDto> items, int totalPrice) {
        return CartResponseDto.builder()
                .cartCount(items.size())
                .totalPrice(totalPrice)
                .items(items)
                .build();
    }
}
