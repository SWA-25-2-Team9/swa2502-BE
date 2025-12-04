package com.swa2502.backend.repository;

import com.swa2502.backend.domain.Member;
import com.swa2502.backend.domain.Order;
import com.swa2502.backend.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findTopByMemberAndOrderItems_StatusInOrderByCreatedAtDesc(Member member, List<OrderStatus> statuses);
    
    List<Order> findDistinctByMemberAndOrderItems_StatusInOrderByCreatedAtDesc(Member member, List<OrderStatus> statuses);

    @Query("SELECT MAX(o.orderNumber) FROM Order o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay")
    Integer findMaxOrderNumberByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
