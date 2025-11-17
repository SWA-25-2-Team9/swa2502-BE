package com.swa2502.backend.controller;

import com.swa2502.backend.dto.MemberDto;
import com.swa2502.backend.security.JwtToken;
import com.swa2502.backend.dto.LogInDto;
import com.swa2502.backend.dto.SignUpDto;
import com.swa2502.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        log.info("----- 회원가입 API 호출됨 -----");
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @Operation(summary = "로그인", description = "로그인하고 JWT 토큰을 발급받습니다.")
    @PostMapping("/log-in")
    public ResponseEntity<JwtToken> logIn(@RequestBody LogInDto logInDto) {
        log.info("----- 로그인 API 호출됨: {} -----", logInDto.getUsername());
        JwtToken jwtToken = memberService.logIn(logInDto);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return ResponseEntity.ok(jwtToken);
    }

    @Operation(summary = "User 테스트", description = "사용자가 User인지 검증합니다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/testUser")
    public String testUser() {
        return "USER ONLY";
    }

    @Operation(summary = "Admin 테스트", description = "사용자가 Admin인지 검증합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/testAdmin")
    public String adminOnly() {
        return "ADMIN ONLY";
    }
}