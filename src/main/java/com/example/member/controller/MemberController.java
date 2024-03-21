package com.example.member.controller;

import com.example.member.dto.MemberRegisterRequestDto;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
