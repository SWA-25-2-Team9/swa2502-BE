package com.swa2502.backend.service;

import com.swa2502.backend.domain.CartItem;
import com.swa2502.backend.domain.Member;
import com.swa2502.backend.domain.MenuItem;
import com.swa2502.backend.dto.CartAddRequestDto;
import com.swa2502.backend.dto.CartAddResponseDto;
import com.swa2502.backend.dto.CartItemResponseDto;
import com.swa2502.backend.dto.CartResponseDto;
import com.swa2502.backend.repository.CartItemRepository;
import com.swa2502.backend.repository.MemberRepository;
import com.swa2502.backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public CartAddResponseDto addToCart(Long memberId, CartAddRequestDto dto) {
        Member member = getMember(memberId);
        MenuItem menuItem = getMenuItem(dto.getMenuId());

        CartItem cartItem = cartItemRepository.save(CartItem.from(member, menuItem, dto));

        return CartAddResponseDto.toDto(cartItem.getId(), cartItemRepository.countByMemberId(memberId));
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponseDto getCart(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(memberId);

        int totalPrice = 0;
        List<CartItemResponseDto> resultItems = new java.util.ArrayList<>();

        for (CartItem ci : cartItems) {
            CartItemResponseDto dto = CartItemResponseDto.toDto(ci);
            totalPrice += dto.getPrice();

            resultItems.add(dto);
        }

        return CartResponseDto.toDto(resultItems, totalPrice);
    }

    @Override
    public CartResponseDto updateQuantity(Long memberId, Long cartItemId, int quantity) {
        CartItem cartItem = getCartItem(cartItemId);

        validateOwner(memberId, cartItem);

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return getCart(memberId);
    }

    @Override
    public int deleteItem(Long memberId, Long cartItemId) {
        CartItem cartItem = getCartItem(cartItemId);

        validateOwner(memberId, cartItem);

        cartItemRepository.delete(cartItem);
        return cartItemRepository.countByMemberId(memberId);
    }

    @Override
    public String clearCart(Long memberId) {
        List<CartItem> cartItems = cartItemRepository.findByMemberId(memberId);
        cartItemRepository.deleteAll(cartItems);
        return "cleared";
    }



    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
    }

    private MenuItem getMenuItem(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found: " + id));
    }

    private CartItem getCartItem(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + id));
    }

    private void validateOwner(Long memberId, CartItem cartItem) {
        if (!cartItem.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("Unauthorized access: memberId=" + memberId);
        }
    }
}
