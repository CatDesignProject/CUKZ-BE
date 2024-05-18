package com.example.member.service;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.GlobalException;
import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.dto.MemberResponseDto;
import com.example.member.entity.Member;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public void registerMember(MemberRegisterRequestDto memberRegisterRequestDto) {
        String username = memberRegisterRequestDto.getUsername();
        verifyDuplicatedUsername(username);

        Member member = memberRegisterRequestDto.toMember();
        member.setEncodedPassword(passwordEncoder.encode(memberRegisterRequestDto.getPassword()));
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public boolean isUsernameDuplicated(String username) {

        return memberRepository.findByUsername(username).isPresent();
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long memberId) {
        Member member = findById(memberId);
        return new MemberResponseDto(member);
    }
    @Transactional(readOnly = true)
    public Page<MemberResponseDto> getAllMembers(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Member> members = memberRepository.findAll(pageable);
        return members.map(member -> new MemberResponseDto(member));
    }

    private void verifyDuplicatedUsername(String username) {
        memberRepository.findByUsername(username).ifPresent(member -> {
            throw new GlobalException(BaseErrorCode.DUPLICATED_MEMBER);
        });
    }

    private Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(BaseErrorCode.NOT_FOUND_MEMBER));
    }
}
