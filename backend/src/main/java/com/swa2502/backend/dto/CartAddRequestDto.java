package com.swa2502.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartAddRequestDto {
    private Long menuId;
    private int quantity;
    private List<Long> selectedOptions;
}
