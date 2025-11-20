package com.swa2502.backend.repository;

import com.swa2502.backend.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByMemberId(Long memberId);

    int countByMemberId(Long memberId);
}
