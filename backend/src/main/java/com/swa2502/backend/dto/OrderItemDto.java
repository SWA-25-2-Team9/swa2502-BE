package com.swa2502.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private String menuName;
    private Integer quantity;
    private Integer price;
    private List<String> options; // Option names for display
}
