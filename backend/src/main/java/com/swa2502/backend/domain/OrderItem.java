package com.swa2502.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer price;

    @ElementCollection
    private List<Long> selectedOptionIds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;


    public static OrderItem createOrderItem(MenuItem menuItem, int quantity, int price, List<Long> selectedOptionIds) {
        return OrderItem.builder()
                .menuItem(menuItem)
                .quantity(quantity)
                .price(price)
                .selectedOptionIds(selectedOptionIds)
                .status(OrderStatus.PENDING)
                .build();
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
