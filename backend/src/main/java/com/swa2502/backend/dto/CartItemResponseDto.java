package com.swa2502.backend.dto;

import com.swa2502.backend.domain.CartItem;
import lombok.*;

import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDto {
    private Long cartItemId;
    private String menuName;
    private int quantity;
    private String optionsText;
    private int price;

    static public CartItemResponseDto toDto(CartItem ci) {

        // 옵션 문자열 단순 처리
        String optionsText = (ci.getSelectedOptionIds() != null)
                ? ci.getSelectedOptionIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                : "";

        // 단가 × 수량 (옵션 가격 미반영)
        int price = ci.getMenuItem().getPrice() * ci.getQuantity();

        return CartItemResponseDto.builder()
                .cartItemId(ci.getId())
                .menuName(ci.getMenuItem().getName())
                .quantity(ci.getQuantity())
                .optionsText(optionsText)
                .price(price)
                .build();
    }
}
