package com.example.member.dto;

import com.example.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String username;
    private String nickname;
    private String role;
    private String email;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.nickname = member.getNickname();
        this.role = member.getRole().name();
        this.email = member.getEmail();
    }
}
