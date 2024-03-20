package com.example.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Authentication의  Principal에 저장될 Custom principal(인증객체)
 */
@Getter
@AllArgsConstructor
public class AuthenticatedMember {

    private Long memberId;
    private String nickName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.hashCode() != obj.hashCode()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, nickName);
    }

}