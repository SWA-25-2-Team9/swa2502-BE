package com.swa2502.backend.dto;

import com.swa2502.backend.domain.Member;
import com.swa2502.backend.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    private String userId;
    private String password;
    private String name;
    private Role role;
    private Long shopId;

    public Member toEntity(String encodedPassword) {

        return Member.builder()
                .userId(userId)
                .password(encodedPassword)
                .name(name)
                .role(role)
                .build();
    }
}
