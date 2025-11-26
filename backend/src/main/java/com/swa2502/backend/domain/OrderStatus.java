package com.swa2502.backend.domain;

public enum OrderStatus {
    PENDING,    // 대기
    ACCEPTED,   // 접수
    COOKING,    // 조리 중
    READY,      // 조리 완료
    PICKED_UP,  // 픽업 완료
    CANCELED    // 취소
}
