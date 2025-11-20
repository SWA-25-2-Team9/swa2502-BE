package com.swa2502.backend.controller;

import com.swa2502.backend.dto.SignUpResponseDto;
import com.swa2502.backend.exception.ErrorResponse;
import com.swa2502.backend.security.LoginResponseDto;
import com.swa2502.backend.dto.LoginRequestDto;
import com.swa2502.backend.dto.SignUpRequestDto;
import com.swa2502.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(schema = @Schema(implementation = SignUpResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "이미 사용 중인 사용자명",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        log.info("----- 회원가입 API 호출됨 -----");
        SignUpResponseDto savedSignUpResponseDto = memberService.signUp(signUpRequestDto);
        return ResponseEntity.ok(savedSignUpResponseDto);
    }

    @Operation(summary = "로그인", description = "로그인하고 JWT 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "존재하지 않는 사용자 또는 비밀번호 불일치",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/log-in")
    public ResponseEntity<LoginResponseDto> logIn(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("----- 로그인 API 호출됨: {} -----", loginRequestDto.getUserId());
        LoginResponseDto loginResponseDto = memberService.logIn(loginRequestDto);
        log.info("jwtToken accessToken = {}, refreshToken = {}", loginResponseDto.getAccessToken(), loginResponseDto.getRefreshToken());
        return ResponseEntity.ok(loginResponseDto);
    }

    @Operation(summary = "ROLE_USER 테스트", description = "사용자 role이 ROLE_USER인지 검증합니다.")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/testUser")
    public String testUser() {
        return "USER ONLY";
    }

    @Operation(summary = "ROLE_ADMIN 테스트", description = "사용자 role이 ROLE_ADMIN인지 검증합니다.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/testAdmin")
    public String adminOnly() {
        return "ADMIN ONLY";
    }
}