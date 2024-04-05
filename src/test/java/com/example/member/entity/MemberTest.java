package com.example.member.entity;

import com.example.member.dto.MemberRegisterRequestDto;
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


    }
}