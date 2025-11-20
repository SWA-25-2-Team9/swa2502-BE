package com.swa2502.backend.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAddResponseDto {
    private Long cartItemId;
    private int cartCount;

    static public CartAddResponseDto toDto(Long cartItemId, int cartCount) {
        return CartAddResponseDto.builder()
                .cartItemId(cartItemId)
                .cartCount(cartCount)
                .build();
    }
}
