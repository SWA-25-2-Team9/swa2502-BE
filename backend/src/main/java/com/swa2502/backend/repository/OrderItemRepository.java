package com.swa2502.backend.repository;

import com.swa2502.backend.domain.OrderItem;
import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT COUNT(oi) FROM OrderItem oi JOIN oi.order o WHERE oi.menuItem.shop = :shop AND oi.status IN :statuses AND o.createdAt < :createdAt")
    long countByShopAndStatusInAndCreatedAtBefore(@Param("shop") Shop shop, @Param("statuses") List<OrderStatus> statuses, @Param("createdAt") LocalDateTime createdAt);

    long countByMenuItem_ShopAndStatusIn(Shop shop, List<OrderStatus> statuses);

    List<OrderItem> findByMenuItem_ShopInAndStatusIn(List<Shop> shops, List<OrderStatus> statuses);
}
