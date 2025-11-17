package com.swa2502.backend.dto;

import com.swa2502.backend.domain.Member;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String username;

    static public MemberDto toDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .username(member.getUsername()).build();
    }

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .username(username).build();
    }
}
