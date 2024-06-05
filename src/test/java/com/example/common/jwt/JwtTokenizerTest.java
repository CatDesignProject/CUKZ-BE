package com.example.common.jwt;

import io.jsonwebtoken.Claims;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class JwtTokenizerTest {

    JwtTokenizer jwtTokenizer = new JwtTokenizer();

    @Test
    void test() {
        // given - memberId, email & encrypt
        Long memberId = 1L;
        String email = "email@email.com";

        // when - encrypt -> parse
        String jwt = jwtTokenizer.createToken(memberId, email);
        Claims claims = jwtTokenizer.parseToken(jwt);

        // then - memberId & email == parse ?
        Long findMemberId = claims.get("memberId", Long.class);
        String findEmail = claims.get("email", String.class);

        Assertions.assertThat(findMemberId).isEqualTo(memberId);
        Assertions.assertThat(findEmail).isEqualTo(email);

        System.out.println("exp: " + claims.getExpiration());
        System.out.println("memberId: " + findMemberId);
        System.out.println("email: " + findEmail);
    }

}