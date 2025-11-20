package com.swa2502.backend.controller;

import com.swa2502.backend.dto.*;
import com.swa2502.backend.service.CartService;
import com.swa2502.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CartAddResponseDto> addToCart(@RequestBody CartAddRequestDto request) {
        // memberId는 JWT에서 추출하거나 request에서 받도록 구현
        CartAddResponseDto response = cartService.addToCart(memberService.getMemberId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getCart() {
        CartResponseDto response = cartService.getCart(memberService.getMemberId());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartResponseDto> updateQuantity(@PathVariable Long cartItemId, @RequestBody CartItemUpdateDto request) {
        CartResponseDto response = cartService.updateQuantity(memberService.getMemberId(), cartItemId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Map<String, Integer>> deleteItem(@PathVariable Long cartItemId) {
        int cartCount = cartService.deleteItem(memberService.getMemberId(), cartItemId);
        return ResponseEntity.ok(Map.of("cartCount", cartCount));
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart() {
        String message = cartService.clearCart(memberService.getMemberId());
        return ResponseEntity.ok(Map.of("message", message));
    }
}
