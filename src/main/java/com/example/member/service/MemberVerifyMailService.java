package com.example.member.service;

import com.example.common.jwt.JwtTokenizer;
import com.example.member.dto.VerifyMailDto;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberVerifyMailService {

    private final JwtTokenizer jwtTokenizer;
    private final String domain = "http://3.35.203.198:8080?token=";

    public boolean requestVerification(VerifyMailDto verifyMailDto) {
        String verificationLink = createVerificationLink(verifyMailDto.getEmail());
        System.out.println(verificationLink);
        String mailTemplate = createMailTemplate(verificationLink);
        sendEmail(mailTemplate);
        return false;
    }

    public boolean verify() {
        // 메일 인증 링크 토큰 검증
        return false;
    }

    private String createVerificationLink(String email) { // memberId, email 으로 JWT 토큰 생성 및 메일 인증링크 생성
        Long memberId = ((AuthenticatedMember) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getMemberId();
        String jwt = jwtTokenizer.createToken(memberId, email);
        return domain + jwt;
    }

    private String createMailTemplate(String link) {
        // 인증 링크 포함한 이메일 문서 생성
        return "";
    }

    private void sendEmail(String mail) {
        // 이메일 발송
    }
}
