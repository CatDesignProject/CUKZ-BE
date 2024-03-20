package com.example.security.authentication;

import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. DB에서 username으로 Member를 찾음
        MemberAuthenticationToken authenticationToken = (MemberAuthenticationToken) authentication;
        String username = String.valueOf(authenticationToken.getPrincipal());

        Member findMember = memberRepository.findByUsername(username).orElseGet(()-> {
                    throw new UsernameNotFoundException("아이디를 찾을 수 없습니다.");
        });

        // 2. DB와 password가 일치하는지 검증
        String password = authenticationToken.getCredential();
        if (!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시 Authentication 토큰 생성
        AuthenticatedMember authenticatedMember = new AuthenticatedMember(findMember.getId(), findMember.getNickname());
        return MemberAuthenticationToken.authenticated(authenticatedMember);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MemberAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
