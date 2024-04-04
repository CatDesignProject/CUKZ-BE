package com.example.member.controller;

import com.example.common.global.BaseResponse;
import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.dto.VerifyUsernameDto;
import com.example.member.service.MemberService;
import com.example.security.authentication.AuthenticatedMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> registerMember(@RequestBody @Validated MemberRegisterRequestDto memberRegisterRequestDto) {
        memberService.registerMember(memberRegisterRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(HttpStatus.CREATED, "회원가입 완료"));
    }

    @PostMapping("/verify-username")
    public ResponseEntity<BaseResponse<Boolean>> registerMember(@RequestBody VerifyUsernameDto verifyUsernameDto) {
        boolean isUsernameDuplicated = memberService.isUsernameDuplicated(verifyUsernameDto.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(HttpStatus.OK, isUsernameDuplicated));
    }

    @GetMapping("/info")
    public ResponseEntity<BaseResponse<String>> infoMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        AuthenticatedMember member = (AuthenticatedMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("member -> {}", member.toString());
        log.info("authenticatedMember -> {}", authenticatedMember.toString());
        log.info("authorities -> {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponse.of(HttpStatus.OK, authenticatedMember.toString()));
    }

}
