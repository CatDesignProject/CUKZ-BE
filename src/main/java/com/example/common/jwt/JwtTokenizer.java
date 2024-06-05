package com.example.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenizer {

    SecretKey key;

    public JwtTokenizer() {
        this.key = Jwts.SIG.HS256.key().build();
    }

    public String createToken(Long memberId, String email) {
        long expMillis = System.currentTimeMillis() + 600000;

        String jwt = Jwts.builder()
                .expiration((new Date(expMillis)))
                .claim("email", email)
                .claim("memberId", memberId)
                .signWith(key)
                .compact();

        return jwt;
    }

    public Claims parseToken(String jwt) {

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();

        return claims;
    }
}
