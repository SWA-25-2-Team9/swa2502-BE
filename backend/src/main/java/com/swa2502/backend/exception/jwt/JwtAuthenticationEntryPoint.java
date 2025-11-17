package com.swa2502.backend.exception.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swa2502.backend.exception.ErrorCode;
import com.swa2502.backend.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         org.springframework.security.core.AuthenticationException authException)
            throws IOException, ServletException {

        ErrorCode code = ErrorCode.AUTHENTICATION_FAILED;
        log.warn("[401 Unauthorized] {}", authException.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(code);

        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
    }
}
