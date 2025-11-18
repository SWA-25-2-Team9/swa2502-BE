package com.swa2502.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    @Schema(example = "ACCESS_DENIED")
    private String error;

    @Schema(example = "접근 권한이 없습니다.")
    private String message;

    @Schema(example = "403")
    private int status;

    public static ErrorResponse of(ErrorCode code) {
        return ErrorResponse.builder()
                .error(code.getCode())
                .message(code.getMessage())
                .status(code.getStatus().value())
                .build();
    }

    public static ErrorResponse of(ErrorCode code, String customMessage) {
        return ErrorResponse.builder()
                .error(code.getCode())
                .message(customMessage)
                .status(code.getStatus().value())
                .build();
    }
}
