package com.swa2502.backend.controller;

import com.swa2502.backend.dto.*;
import com.swa2502.backend.service.CartService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "장바구니 관련 API")
@PreAuthorize("hasRole('USER')")
public class CartController {
    private final CartService cartService;

    @PostMapping
    @Operation(summary = "장바구니 추가", description = "상품을 장바구니에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공",
                    content = @Content(schema = @Schema(implementation = CartAddResponseDto.class)))
    })
    public ResponseEntity<CartAddResponseDto> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CartAddRequestDto request) {

        // JWT의 subject에서 memberId 추출
        Long memberId = Long.parseLong(userDetails.getUsername());
        CartAddResponseDto response = cartService.addToCart(memberId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "장바구니 조회", description = "현재 사용자의 장바구니를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class)))
    })
    public ResponseEntity<CartResponseDto> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        CartResponseDto response = cartService.getCart(memberId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cartItemId}")
    @Operation(summary = "장바구니 수량 변경", description = "장바구니 항목의 수량을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수량 변경 성공",
                    content = @Content(schema = @Schema(implementation = CartResponseDto.class)))
    })
    public ResponseEntity<CartResponseDto> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId, @RequestBody CartItemUpdateDto request) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        CartResponseDto response = cartService.updateQuantity(memberId, cartItemId, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "장바구니 항목 삭제", description = "장바구니에서 특정 항목을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공")
    })
    public ResponseEntity<Map<String, Integer>> deleteItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        int cartCount = cartService.deleteItem(memberId, cartItemId);
        return ResponseEntity.ok(Map.of("cartCount", cartCount));
    }

    @DeleteMapping
    @Operation(summary = "장바구니 비우기", description = "장바구니의 모든 항목을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비우기 성공")
    })
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long memberId = Long.parseLong(userDetails.getUsername());
        String message = cartService.clearCart(memberId);
        return ResponseEntity.ok(Map.of("message", message));
    }
}
