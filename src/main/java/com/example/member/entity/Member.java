package com.example.member.entity;

import com.example.member.dto.MemberRegisterRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    private String username;

    private String password;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    public static Member fromMemberRegisterRequestDto(MemberRegisterRequestDto memberRegisterRequestDto) {
        Member member = new Member();

        member.username = memberRegisterRequestDto.getUsername();
        member.password = memberRegisterRequestDto.getPassword();
        member.nickname = memberRegisterRequestDto.getNickname();

        return member;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
}

