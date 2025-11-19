package com.swa2502.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDetailDto {
    private Long menuId;
    private String name;
    private Integer price;
    private List<OptionGroupDto> optionGroups;
}
