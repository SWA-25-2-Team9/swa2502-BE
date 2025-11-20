package com.swa2502.backend.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {
    private String grantType;   // Bearer
    private String accessToken;
    private String refreshToken;
}