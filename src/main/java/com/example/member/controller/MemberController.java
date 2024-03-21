package com.example.member.controller;

import com.example.member.dto.MemberRegisterRequestDto;
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
    public ResponseEntity registerMember(@RequestBody @Validated MemberRegisterRequestDto memberRegisterRequestDto) {
        memberService.registerMember(memberRegisterRequestDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/info")
    public void infoMember(@AuthenticationPrincipal AuthenticatedMember authenticatedMember) {
        AuthenticatedMember member = (AuthenticatedMember) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("member -> {}", member.toString());
        log.info("authenticatedMember -> {}", authenticatedMember.toString());
    }

}
