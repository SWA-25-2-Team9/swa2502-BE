package com.swa2502.backend.domain;

public enum OrderStatus {
    ACCEPTED,   // 접수
    READY,      // 준비 완료
    PICKED_UP,  // 수령 완료
    CANCELED    // 취소
}
