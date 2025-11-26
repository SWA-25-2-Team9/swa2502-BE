package com.swa2502.backend.repository;

import com.swa2502.backend.domain.Member;
import com.swa2502.backend.domain.Order;
import com.swa2502.backend.domain.OrderStatus;
import com.swa2502.backend.domain.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findTopByMemberAndStatusInOrderByCreatedAtDesc(Member member, List<OrderStatus> statuses);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.shop = :shop AND o.status IN :statuses AND o.createdAt < :createdAt")
    long countByShopAndStatusInAndCreatedAtBefore(@Param("shop") Shop shop, @Param("statuses") List<OrderStatus> statuses, @Param("createdAt") LocalDateTime createdAt);

    @Query("SELECT MAX(o.orderNumber) FROM Order o WHERE o.shop = :shop AND o.createdAt >= :startOfDay AND o.createdAt < :endOfDay")
    Integer findMaxOrderNumberByShopAndDate(@Param("shop") Shop shop, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
