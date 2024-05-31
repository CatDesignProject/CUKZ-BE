package com.example.member.service;

import com.example.member.dto.VerifyMailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberVerifyMailService {

    public boolean requestVerification(VerifyMailDto verifyMailDto) {
        String verificationLink = createVerificationLink(verifyMailDto.getEmail());
        String mailTemplate = createMailTemplate(verificationLink);
        sendEmail(mailTemplate);
        return false;
    }

    public boolean verify() {
        // 메일 인증 링크 토큰 검증
        return false;
    }

    private String createVerificationLink(String email) {
        // SecurityContext에서 memberId 꺼내기
        // JWT 토큰 생성 시 memberId, email 넘겨주기
        // 토큰 Redis에 저장하기
        return ""; // 링크에 JWT 이어붙여 리턴
    }

    private String createMailTemplate(String link) {
        // 인증 링크 포함한 이메일 문서 생성
        return "";
    }

    private void sendEmail(String mail) {
        // 이메일 발송
    }
}
