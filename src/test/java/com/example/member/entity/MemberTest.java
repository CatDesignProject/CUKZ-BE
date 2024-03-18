package com.example.member.entity;

import com.example.member.dto.MemberRegisterDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void memberTest() {
        // given
        MemberRegisterDto dto = MemberRegisterDto.builder()
                .username("username123")
                .password("password123")
                .nickname("nickname123")
                .build();

        // when
        Member member = Member.fromMemberRegisterDto(dto);

        // then
        Assertions.assertThat(member.getUsername()).isEqualTo(dto.getUsername());
        Assertions.assertThat(member.getPassword()).isEqualTo(dto.getPassword());
        Assertions.assertThat(member.getNickname()).isEqualTo(dto.getNickname());
    }
}