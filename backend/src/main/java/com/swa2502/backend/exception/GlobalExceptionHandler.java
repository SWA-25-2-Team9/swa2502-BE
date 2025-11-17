package com.swa2502.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode code = ex.getErrorCode();

        log.warn("[CustomException] {} - {}", code.getCode(), ex.getMessage());

        return ResponseEntity
                .status(code.getStatus())
                .body(ErrorResponse.of(code, ex.getMessage()));
    }

    // 인증/인가 실패 (Spring Security)
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(Exception ex) {

        ErrorCode code = ErrorCode.ACCESS_DENIED;

        log.warn("----- [AccessDenied] {} -----", ex.getMessage());

        return ResponseEntity
                .status(code.getStatus())
                .body(ErrorResponse.of(code));
    }

    // 기타 서버 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception ex) {
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;

        log.error("[Exception] ", ex);

        return ResponseEntity
                .status(code.getStatus())
                .body(ErrorResponse.of(code));
    }
}
