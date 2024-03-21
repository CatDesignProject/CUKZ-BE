package com.example.security.authentication;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class MemberAuthenticationToken extends AbstractAuthenticationToken {

    private Object principal;
    private String credential;

    private MemberAuthenticationToken(String username, String password) {
        super(null);
        this.principal = username;
        this.credential = password;
        this.setAuthenticated(false);
    }

    private MemberAuthenticationToken(Object principal, String credential) {
        super(null);
        this.principal = principal;
        this.credential = null;
        this.setAuthenticated(true);
    }

    // 인증처리 전
    public static MemberAuthenticationToken unauthenticated(UsernamePasswordLoginDto loginDto) {
        return new MemberAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
    }

    // 인증처리 후
    public static MemberAuthenticationToken authenticated(AuthenticatedMember principal) {
        return new MemberAuthenticationToken(principal, null);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
