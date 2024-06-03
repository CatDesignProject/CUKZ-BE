package com.example.member.service;

import com.example.common.jwt.JwtTokenizer;
import com.example.common.mail.CustomMailSender;
import com.example.common.mail.MailType;
import com.example.member.dto.VerifyMailDto;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberVerifyMailService {

    private final JwtTokenizer jwtTokenizer;
    private final CustomMailSender customMailSender;
    private final String domain = "http://3.35.203.198:8080?token=";

    public boolean requestVerification(VerifyMailDto verifyMailDto) {
        String verificationLink = createVerificationLink(verifyMailDto.getEmail());
        return sendMail(verifyMailDto.getEmail(), verificationLink);
    }

    public boolean verify() {
        // 메일 인증 링크 토큰 검증
        return false;
    }

    private String createVerificationLink(String email) { // memberId, email 으로 JWT 토큰 생성 및 메일 인증링크 생성
        AuthenticatedMember member = (AuthenticatedMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String jwt = jwtTokenizer.createToken(member.getMemberId(), email);
        return domain + jwt;
    }

    private boolean sendMail(String email, String link) { // 이메일 발송
        AuthenticatedMember member = (AuthenticatedMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, String> emailValues = new HashMap<>();

        emailValues.put("nickname", member.getNickName());
        emailValues.put("verificationLink", link);

        return customMailSender.sendMail(email, MailType.VERIFY_EMAIL, emailValues);
    }
}
