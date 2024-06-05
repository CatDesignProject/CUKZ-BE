package com.example.member.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.common.jwt.JwtTokenizer;
import com.example.common.mail.CustomMailSender;
import com.example.common.mail.MailType;
import com.example.member.dto.VerifyMailDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import com.example.security.authentication.AuthenticatedMember;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberVerifyMailService {

    private final JwtTokenizer jwtTokenizer;
    private final CustomMailSender customMailSender;
    private final MemberRepository memberRepository;
    private final String domain = "http://3.35.203.198:8080/verify-email?token=";

    public boolean requestVerification(VerifyMailDto verifyMailDto) {
        String verificationLink = createVerificationLink(verifyMailDto.getEmail());
        return sendMail(verifyMailDto.getEmail(), verificationLink);
    }

    public Map<String, String> verify(String token) { // 메일 인증 링크 토큰 검증
        Map<String, String> map = new HashMap<>();
        Claims claims;

        try {
            claims = jwtTokenizer.parseToken(token);
        } catch (Exception e) {
            throw new GlobalException(BaseErrorCode.INVALID_EMAIL);
        }

        String email = (String) claims.get("email");
        Long memberId = Long.valueOf(String.valueOf(claims.get("memberId")));

        String nickname = promoteManager(memberId, email);
        map.put("email", email);
        map.put("nickname", nickname);

        return map;
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

    @Transactional
    protected String promoteManager(Long memberId, String email) { // 총대 승격
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(BaseErrorCode.NOT_FOUND_MEMBER));
        member.promoteManager(email);
        memberRepository.save(member);
        return member.getNickname();
    }
}
