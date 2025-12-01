package com.swa2502.backend.controller;

import com.swa2502.backend.dto.MenuDto;
import com.swa2502.backend.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
@Tag(name = "Shop", description = "가게 관련 API")
@PreAuthorize("hasRole('USER')")
public class ShopController {

    private final MenuService menuService;

    @GetMapping("/{shopId}/menus")
    @Operation(summary = "가게 메뉴 조회", description = "특정 가게의 모든 메뉴를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MenuDto.class)))
    })
    public ResponseEntity<List<MenuDto>> getMenusByShopId(@PathVariable("shopId") Long shopId) {
        List<MenuDto> menus = menuService.getMenusByShopId(shopId);
        return ResponseEntity.ok(menus);
    }
}
