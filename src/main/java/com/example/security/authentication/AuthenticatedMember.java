package com.example.security.authentication;

import com.example.member.entity.MemberRole;
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
    private MemberRole role;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        AuthenticatedMember authenticatedMember = (AuthenticatedMember) obj;
        return Objects.equals(this.memberId, authenticatedMember.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("memberId: ").append(memberId)
                .append(", nickname: ").append(nickName)
                .append(", role: ").append(role.name());
        return sb.toString();
    }
}