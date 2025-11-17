package com.swa2502.backend.exception.jwt;

import com.swa2502.backend.exception.jwt.JwtExpiredException;
import com.swa2502.backend.exception.jwt.JwtInvalidException;
import com.swa2502.backend.exception.jwt.JwtSignatureException;
import com.swa2502.backend.security.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final List<String> EXCLUDE_URLS = Arrays.asList(
            "/members/log-in",
            "/members/sign-up",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDE_URLS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String token = resolveToken(request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("----- JWT 인증 성공: user={} path={} -----", auth.getName(), requestURI);
            }
        } catch (ExpiredJwtException e) {
            log.warn("----- [JWT 만료] token={} path={} msg={} -----", token, requestURI, e.getMessage());
            throw new JwtExpiredException();

        } catch (MalformedJwtException e) {
            log.warn("----- [JWT 형식 오류] token={} path={} msg={} -----", token, requestURI, e.getMessage());
            throw new JwtInvalidException();

        } catch (SignatureException e) {
            log.warn("----- [JWT 서명 불일치] token={} path={} msg={} -----", token, requestURI, e.getMessage());
            throw new JwtSignatureException();

        } catch (Exception e) {
            log.error("----- [JWT 오류] path={} msg={} -----", requestURI, e.getMessage());
            throw new JwtInvalidException(); // 통합된 기타 오류 처리
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
