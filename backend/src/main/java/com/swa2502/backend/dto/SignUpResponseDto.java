package com.swa2502.backend.dto;

import com.swa2502.backend.domain.Member;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponseDto {

    private Long memberId;
    private String userId;
    private String name;
    private String role;

    static public SignUpResponseDto from(Member member) {
        return SignUpResponseDto.builder()
                .memberId(member.getId())
                .userId(member.getUserId())
                .name(member.getName())
                .role(member.getRole().name())
                .build();
    }
}
