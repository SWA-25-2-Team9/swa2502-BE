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
public class OptionGroupDto {
    private Long groupId;
    private String name;
    private Boolean required;
    private Integer min;
    private Integer max;
    private List<OptionDto> options;
}
