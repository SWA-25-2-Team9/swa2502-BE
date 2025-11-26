package com.swa2502.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "요청 값이 올바르지 않습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 내부 오류입니다."),

    // 인증/인가
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", "인증에 실패했습니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER_NOT_FOUND", "존재하지 않는 사용자입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "PASSWORD_MISMATCH", "비밀번호가 일치하지 않습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT_EXPIRED", "JWT 토큰이 만료되었습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "JWT_INVALID", "유효하지 않은 JWT 토큰입니다."),
    JWT_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "JWT_SIGNATURE_ERROR", "JWT 서명 검증에 실패했습니다."),

    // 사용자
    DUPLICATE_USER(HttpStatus.CONFLICT, "DUPLICATE_USER", "이미 사용 중인 사용자명입니다."),
    
    // 주문/장바구니
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "주문을 찾을 수 없습니다."),
    CART_EMPTY(HttpStatus.BAD_REQUEST, "CART_EMPTY", "장바구니가 비어있습니다."),
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "SHOP_NOT_FOUND", "상점을 찾을 수 없습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "CART_ITEM_NOT_FOUND", "장바구니 항목을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
