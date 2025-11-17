package com.swa2502.backend.service;

import com.swa2502.backend.dto.LogInDto;
import com.swa2502.backend.dto.MemberDto;
import com.swa2502.backend.security.JwtToken;
import com.swa2502.backend.dto.SignUpDto;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    @Transactional
    MemberDto signUp(SignUpDto signUpDto);

    @Transactional
    JwtToken logIn(LogInDto logInDto);
}
