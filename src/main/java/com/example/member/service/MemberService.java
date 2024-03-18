package com.example.member.service;

import com.example.member.dto.MemberRegisterDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void registerMember(MemberRegisterDto memberRegisterDto) {
        Member member = Member.fromMemberRegisterDto(memberRegisterDto);
        memberRepository.save(member);
    }

}
