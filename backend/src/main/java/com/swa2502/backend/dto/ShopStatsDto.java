package com.swa2502.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopStatsDto {
    private Long todaySales;
    private Long orderCount;
    private Long avgPrepTime; // in minutes
    private Long batchOrderCount;
}
