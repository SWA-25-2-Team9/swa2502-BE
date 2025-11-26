package com.swa2502.backend.service;

import com.swa2502.backend.domain.*;
import com.swa2502.backend.dto.*;
import com.swa2502.backend.repository.*;
import com.swa2502.backend.exception.CustomException;
import com.swa2502.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuOptionRepository menuOptionRepository;

    @Transactional
    public OrderResponseDto createOrder(Long memberId, OrderRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new CustomException(ErrorCode.SHOP_NOT_FOUND));

        List<CartItem> cartItems = member.getCartItems();
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);
        }

        List<CartItem> itemsForShop = cartItems.stream()
                .filter(item -> item.getMenuItem().getShop().getId().equals(shop.getId()))
                .collect(Collectors.toList());

        if (itemsForShop.isEmpty()) {
             throw new CustomException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        int totalPrice = itemsForShop.stream()
                .mapToInt(item -> {
                    int itemPrice = item.getMenuItem().getPrice();
                    int optionsPrice = 0;
                    if (item.getSelectedOptionIds() != null) {
                        List<MenuOption> options = menuOptionRepository.findAllById(item.getSelectedOptionIds());
                        optionsPrice = options.stream().mapToInt(MenuOption::getExtraPrice).sum();
                    }
                    return (itemPrice + optionsPrice) * item.getQuantity();
                })
                .sum();

        // Order Number 생성
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);
        Integer maxOrderNumber = orderRepository.findMaxOrderNumberByShopAndDate(shop, startOfDay, endOfDay);
        int orderNumber = (maxOrderNumber == null) ? 1 : maxOrderNumber + 1;

        Order order = Order.builder()
                .member(member)
                .shop(shop)
                .status(OrderStatus.PENDING)
                .totalPrice(totalPrice)
                .orderNumber(orderNumber)
                .specialRequest(request.getRequest())
                .orderItems(new ArrayList<>())
                .build();

        // OrderItems 생성
        for (CartItem cartItem : itemsForShop) {
            int itemPrice = cartItem.getMenuItem().getPrice();
            if (cartItem.getSelectedOptionIds() != null) {
                List<MenuOption> options = menuOptionRepository.findAllById(cartItem.getSelectedOptionIds());
                itemPrice += options.stream().mapToInt(MenuOption::getExtraPrice).sum();
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(cartItem.getMenuItem())
                    .quantity(cartItem.getQuantity())
                    .price(itemPrice)
                    .selectedOptionIds(new ArrayList<>(cartItem.getSelectedOptionIds()))
                    .build();
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);

        // Cart Clear
        cartItemRepository.deleteAll(itemsForShop);
        member.getCartItems().removeAll(itemsForShop);

        // MyTurn and ETA 계산
        long myTurn = calculateMyTurn(shop, order.getCreatedAt());
        int estimatedReadyMin = calculateEstimatedReadyMin(shop, myTurn);

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(orderNumber)
                .myTurn(myTurn)
                .estimatedReadyMin(estimatedReadyMin)
                .build();
    }

    public CurrentOrderResponseDto getCurrentOrder(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<OrderStatus> activeStatuses = List.of(OrderStatus.PENDING, OrderStatus.COOKING, OrderStatus.READY);
        Order order = orderRepository.findTopByMemberAndStatusInOrderByCreatedAtDesc(member, activeStatuses)
                .orElse(null);

        if (order == null) {
            return null;
        }

        long myTurn = calculateMyTurn(order.getShop(), order.getCreatedAt());
        int estimatedReadyMin = calculateEstimatedReadyMin(order.getShop(), myTurn);

        List<OrderItemDto> itemDtos = order.getOrderItems().stream()
                .map(item -> {
                    List<String> optionNames = new ArrayList<>();
                    if (item.getSelectedOptionIds() != null && !item.getSelectedOptionIds().isEmpty()) {
                        List<MenuOption> options = menuOptionRepository.findAllById(item.getSelectedOptionIds());
                        optionNames = options.stream().map(MenuOption::getName).collect(Collectors.toList());
                    }
                    return OrderItemDto.builder()
                            .menuName(item.getMenuItem().getName())
                            .quantity(item.getQuantity())
                            .price(item.getPrice())
                            .options(optionNames)
                            .build();
                })
                .collect(Collectors.toList());

        return CurrentOrderResponseDto.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .shopName(order.getShop().getName())
                .status(order.getStatus())
                .myTurn(myTurn)
                .estimatedReadyMin(estimatedReadyMin)
                .totalPrice(order.getTotalPrice())
                .orderedAt(order.getCreatedAt())
                .items(itemDtos)
                .build();
    }

    private long calculateMyTurn(Shop shop, LocalDateTime createdAt) {
        List<OrderStatus> waitingStatuses = List.of(OrderStatus.PENDING, OrderStatus.COOKING);
        return orderRepository.countByShopAndStatusInAndCreatedAtBefore(shop, waitingStatuses, createdAt);
    }

    // 임시 ETA 계산
    private int calculateEstimatedReadyMin(Shop shop, long myTurn) {
        int avgCookingTime = 5; // Default 5 mins
        if (shop.getEtaMinutes() != null && shop.getEtaMinutes() > 0) {
        }
        return (int) ((myTurn + 1) * avgCookingTime);
    }
}
