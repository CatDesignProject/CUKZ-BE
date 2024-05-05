package com.example.member.controller;

import com.example.common.global.BaseResponse;
import com.example.common.global.PageResponseDto;
import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.dto.MemberResponseDto;
import com.example.member.dto.VerifyUsernameDto;
import com.example.member.service.MemberService;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members/register")
    public ResponseEntity<BaseResponse<String>> registerMember(@RequestBody @Validated MemberRegisterRequestDto memberRegisterRequestDto) {
        memberService.registerMember(memberRegisterRequestDto);
        return ResponseEntity.ok()
                .body(BaseResponse.of(HttpStatus.CREATED, "회원가입 완료"));
    }

    @PostMapping("/members/verify-username")
    public ResponseEntity<BaseResponse<Boolean>> registerMember(@RequestBody VerifyUsernameDto verifyUsernameDto) {
        boolean isUsernameDuplicated = memberService.isUsernameDuplicated(verifyUsernameDto.getUsername());
        return ResponseEntity.ok()
                .body(BaseResponse.of(HttpStatus.OK, isUsernameDuplicated));
    }

    @GetMapping("/members/me")
    public ResponseEntity<BaseResponse<MemberResponseDto>> infoMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        AuthenticatedMember member = (AuthenticatedMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MemberResponseDto responseDto = memberService.getMember(member.getMemberId());

        return ResponseEntity.ok()
                .body(BaseResponse.of(HttpStatus.OK, responseDto));
    }

    @GetMapping("/admin/members")
    public ResponseEntity<BaseResponse> findAllMembers(@RequestParam(defaultValue = "1", name = "page") int page,
                                                       @RequestParam(defaultValue = "10", name = "size") int size) {
        Page<MemberResponseDto> memberResponseDtos = memberService.getAllMembers(page - 1, size);

        return ResponseEntity.ok()
                .body(BaseResponse.of(HttpStatus.OK, PageResponseDto.toResponseDto(memberResponseDtos)));
    }
}
