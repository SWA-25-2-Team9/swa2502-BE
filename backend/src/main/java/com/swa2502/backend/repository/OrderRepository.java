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

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE o.member = :member AND oi.status IN :statuses ORDER BY o.createdAt DESC")
    Optional<Order> findTopByMemberAndOrderItems_StatusInOrderByCreatedAtDesc(@Param("member") Member member, @Param("statuses") List<OrderStatus> statuses);

    @Query("SELECT MAX(o.orderNumber) FROM Order o WHERE o.createdAt BETWEEN :startOfDay AND :endOfDay")
    Integer findMaxOrderNumberByDate(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
