package com.swa2502.backend.security;

import com.swa2502.backend.domain.Member;
import com.swa2502.backend.dto.JwtToken;
import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private Long memberId;
    private String userId;
    private String name;
    private String role;

    private String accessToken;
    private String refreshToken;


    static public LoginResponseDto from(Member member, JwtToken jwtToken) {
        return LoginResponseDto.builder()
                .memberId(member.getId())
                .userId(member.getUserId())
                .name(member.getName())
                .role(member.getRole().name())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}