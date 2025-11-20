package com.swa2502.backend.controller;

import com.swa2502.backend.dto.MenuDto;
import com.swa2502.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final MenuService menuService;

    @GetMapping("/{shopId}/menus")
    public ResponseEntity<List<MenuDto>> getMenusByShopId(@PathVariable("shopId") Long shopId) {
        List<MenuDto> menus = menuService.getMenusByShopId(shopId);
        return ResponseEntity.ok(menus);
    }
}
