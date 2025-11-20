package com.swa2502.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopWithQueueDto {
    private Long shopId;
    private String name;
    private Integer currentQueue;
    private Integer etaMinutes;
    private Boolean isOpen;
}
