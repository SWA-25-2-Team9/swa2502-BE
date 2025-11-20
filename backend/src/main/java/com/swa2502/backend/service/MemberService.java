package com.swa2502.backend.service;

import com.swa2502.backend.dto.LoginRequestDto;
import com.swa2502.backend.dto.SignUpResponseDto;
import com.swa2502.backend.security.LoginResponseDto;
import com.swa2502.backend.dto.SignUpRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    @Transactional
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);

    @Transactional
    LoginResponseDto logIn(LoginRequestDto loginRequestDto);

}
