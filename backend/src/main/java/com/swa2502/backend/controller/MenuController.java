package com.swa2502.backend.controller;

import com.swa2502.backend.dto.MenuDetailDto;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "메뉴 관련 API")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{menuId}")
    @Operation(summary = "메뉴 상세 조회", description = "특정 메뉴의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = MenuDetailDto.class)))
    })
    public ResponseEntity<MenuDetailDto> getMenuById(@PathVariable("menuId") Long menuId) {
        MenuDetailDto menu = menuService.getMenuById(menuId);
        return ResponseEntity.ok(menu);
    }
}
