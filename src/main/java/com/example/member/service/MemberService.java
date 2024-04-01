package com.example.member.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void registerMember(MemberRegisterRequestDto memberRegisterRequestDto) {
        String username = memberRegisterRequestDto.getUsername();
        verifyDuplicatedUsername(username);

        Member member = memberRegisterRequestDto.toMember();
        member.setEncodedPassword(passwordEncoder.encode(memberRegisterRequestDto.getPassword()));
        memberRepository.save(member);
    }

    public boolean isUsernameDuplicated(String username) {
        return memberRepository.findByUsername(username).isPresent();
    }

    private void verifyDuplicatedUsername(String username) {
        memberRepository.findByUsername(username).ifPresent(member -> {
            throw new GlobalException(BaseErrorCode.DUPLICATED_MEMBER);
        });
    }

}
