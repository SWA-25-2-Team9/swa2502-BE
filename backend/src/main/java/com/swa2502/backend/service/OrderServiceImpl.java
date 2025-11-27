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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuOptionRepository menuOptionRepository;

    @Override
    @Transactional
    public List<OrderResponseDto> createOrder(Long memberId, OrderRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<CartItem> cartItems = member.getCartItems();
        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY);
        }

        // Group cart items by Shop
        Map<Shop, List<CartItem>> itemsByShop = cartItems.stream()
                .collect(Collectors.groupingBy(item -> item.getMenuItem().getShop()));

        List<OrderResponseDto> responses = new ArrayList<>();

        for (Map.Entry<Shop, List<CartItem>> entry : itemsByShop.entrySet()) {
            Shop shop = entry.getKey();
            List<CartItem> itemsForShop = entry.getValue();

            // Order Number 생성
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);
            Integer maxOrderNumber = orderRepository.findMaxOrderNumberByShopAndDate(shop, startOfDay, endOfDay);
            int orderNumber = (maxOrderNumber == null) ? 1 : maxOrderNumber + 1;

            // factory method로 Order 생성
            Order order = Order.createOrder(member, shop, request.getRequest(), orderNumber);

            // factory method로 OrderItems 생성
            for (CartItem cartItem : itemsForShop) {
                int itemPrice = cartItem.getMenuItem().getPrice();
                if (cartItem.getSelectedOptionIds() != null) {
                    List<MenuOption> options = menuOptionRepository.findAllById(cartItem.getSelectedOptionIds());
                    itemPrice += options.stream().mapToInt(MenuOption::getExtraPrice).sum();
                }

                OrderItem orderItem = OrderItem.createOrderItem(
                        cartItem.getMenuItem(),
                        cartItem.getQuantity(),
                        itemPrice,
                        new ArrayList<>(cartItem.getSelectedOptionIds())
                );
                order.addOrderItem(orderItem);
            }

            orderRepository.save(order);

            // MyTurn and ETA 계산
            long myTurn = calculateMyTurn(shop, order.getCreatedAt());
            int estimatedReadyMin = calculateEstimatedReadyMin(shop, myTurn);

            responses.add(OrderResponseDto.builder()
                    .orderId(order.getId())
                    .orderNumber(orderNumber)
                    .myTurn(myTurn)
                    .estimatedReadyMin(estimatedReadyMin)
                    .build());
        }

        // Cart Clear
        cartItemRepository.deleteAll(cartItems);
        member.getCartItems().clear();

        return responses;
    }

    @Override
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

    // 임시 ETA 및 Occupancy 로직
    private int calculateEstimatedReadyMin(Shop shop, long myTurn) {
        // Occupancy logic: if myTurn is high, increase time
        int baseTime = 5; // Default 5 mins
        if (shop.getEtaMinutes() != null && shop.getEtaMinutes() > 0) {
            baseTime = shop.getEtaMinutes();
        }
        
        // Simple occupancy factor: +1 min for every 2 waiting orders
        int occupancyDelay = (int) (myTurn / 2);

        return (int) ((myTurn + 1) * baseTime) + occupancyDelay;
    }
}
