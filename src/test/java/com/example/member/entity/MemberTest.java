package com.example.member.entity;

import com.example.member.dto.MemberRegisterRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("fromMemberRegisterRequestDto 테스트")
    void memberTest() {
        // given
        MemberRegisterRequestDto dto = MemberRegisterRequestDto.builder()
                .username("username123")
                .password("password123")
                .nickname("nickname123")
                .build();

        // when
        Member member = Member.fromMemberRegisterRequestDto(dto);

        // then
        Assertions.assertThat(member.getUsername()).isEqualTo(dto.getUsername());
        Assertions.assertThat(member.getPassword()).isEqualTo(dto.getPassword());
        Assertions.assertThat(member.getNickname()).isEqualTo(dto.getNickname());
    }
}