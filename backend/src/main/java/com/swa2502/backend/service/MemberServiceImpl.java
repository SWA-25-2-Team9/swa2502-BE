package com.swa2502.backend.service;

import com.swa2502.backend.domain.Member;
import com.swa2502.backend.dto.LogInDto;
import com.swa2502.backend.dto.MemberDto;
import com.swa2502.backend.exception.CustomException;
import com.swa2502.backend.exception.ErrorCode;
import com.swa2502.backend.repository.MemberRepository;
import com.swa2502.backend.security.JwtToken;
import com.swa2502.backend.security.JwtTokenProvider;
import com.swa2502.backend.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public MemberDto signUp(SignUpDto signUpDto) {
        if (memberRepository.existsByUsername(signUpDto.getUsername())) {
            throw new CustomException(ErrorCode.DUPLICATE_USER,
                    "이미 사용 중인 사용자명입니다: " + signUpDto.getUsername());
        }
        // Password 암호화
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity(encodedPassword)));
    }

    @Transactional
    @Override
    public JwtToken logIn(LogInDto logInDto) {
        String username = logInDto.getUsername();
        String password = logInDto.getPassword();

        log.info("----- 로그인 시도: username={} -----", username);

        // 1. 사용자 조회
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("존재하지 않는 사용자: {}", username);
                    return new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            log.warn("비밀번호 불일치: {}", username);
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 3. 권한 정보 생성
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(member.getRole().name()));

        // 4. Authentication 객체 생성 (이미 인증된 상태로)
        UserDetails principal = new User(member.getUsername(), "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                "",
                authorities
        );

        // 5. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

        log.info("로그인 성공: {}, 토큰 생성됨", username);

        return jwtToken;
    }

    public Long getMemberId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) auth.getPrincipal();
        return member.getId();
    }
}
