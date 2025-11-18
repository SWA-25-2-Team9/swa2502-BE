package com.swa2502.backend.dto;

import com.swa2502.backend.domain.Member;
import com.swa2502.backend.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String username;
    private String password;
    private Role role;

    public Member toEntity(String encodedPassword) {

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .role(role)
                .build();
    }
}
