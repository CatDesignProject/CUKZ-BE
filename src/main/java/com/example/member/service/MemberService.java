package com.example.member.service;

import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void registerMember(MemberRegisterRequestDto memberRegisterRequestDto) {
        Member member = Member.fromMemberRegisterRequestDto(memberRegisterRequestDto);
        memberRepository.save(member);
    }

}
