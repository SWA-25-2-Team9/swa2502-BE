package com.swa2502.backend.repository;

import com.swa2502.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByuserId(String userId);

    boolean existsByuserId(String userId);
}
