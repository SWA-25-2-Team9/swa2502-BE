package com.swa2502.backend.exception.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa2502.backend.exception.ErrorCode;
import com.swa2502.backend.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.access.AccessDeniedHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException)
            throws IOException {

        ErrorCode code = ErrorCode.ACCESS_DENIED;
        log.warn("[403 Forbidden] {}", accessDeniedException.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(code);

        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
