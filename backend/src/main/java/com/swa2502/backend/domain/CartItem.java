package com.swa2502.backend.domain;

import com.swa2502.backend.dto.CartAddRequestDto;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    private int quantity;

    @ElementCollection
    private List<Long> selectedOptionIds;

    public static CartItem from(Member member, MenuItem menuItem, CartAddRequestDto dto) {
        return CartItem.builder()
                .member(member)
                .menuItem(menuItem)
                .quantity(dto.getQuantity())
                .selectedOptionIds(dto.getSelectedOptions())
                .build();
    }
}
