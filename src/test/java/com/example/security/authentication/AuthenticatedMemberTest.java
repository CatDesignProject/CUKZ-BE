package com.example.security.authentication;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticatedMemberTest {

    @Test
    @DisplayName("AuthenticatedMember equals 테스트")
    void equalsTest() {
        // given
        AuthenticatedMember member1 = new AuthenticatedMember(1L, "nickname");
        AuthenticatedMember member2 = new AuthenticatedMember(2L, "nickname");

        // then
        Assertions.assertThat(member1.equals(member2)).isFalse();
    }

    @Test
    @DisplayName("AuthenticatedMember hashCode 테스트")
    void hashCodeTest() {
        // given
        AuthenticatedMember member1 = new AuthenticatedMember(1L, "nickname");
        AuthenticatedMember member2 = new AuthenticatedMember(1L, "nickname");

        // then
        Assertions.assertThat(member1.hashCode()).isEqualTo(member2.hashCode());
    }
}