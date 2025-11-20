package com.swa2502.backend.controller;

import com.swa2502.backend.dto.MenuDetailDto;
import com.swa2502.backend.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDetailDto> getMenuById(@PathVariable("menuId") Long menuId) {
        MenuDetailDto menu = menuService.getMenuById(menuId);
        return ResponseEntity.ok(menu);
    }
}
