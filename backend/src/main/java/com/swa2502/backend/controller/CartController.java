package com.swa2502.backend.controller;

import com.swa2502.backend.dto.*;
import com.swa2502.backend.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "장바구니 관련 API")
@PreAuthorize("hasRole('USER')")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartAddResponseDto> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartAddRequestDto request) {

        // JWT의 subject에서 memberId 추출
        Long memberId = Long.parseLong(userDetails.getUsername());
        CartAddResponseDto response = cartService.addToCart(memberId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        CartResponseDto response = cartService.getCart(memberId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartResponseDto> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId, @RequestBody CartItemUpdateDto request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        CartResponseDto response = cartService.updateQuantity(memberId, cartItemId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Map<String, Integer>> deleteItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        int cartCount = cartService.deleteItem(memberId, cartItemId);
        return ResponseEntity.ok(Map.of("cartCount", cartCount));
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String message = cartService.clearCart(memberId);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
