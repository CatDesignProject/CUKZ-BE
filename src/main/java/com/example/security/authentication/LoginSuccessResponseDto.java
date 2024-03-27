package com.example.security.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginSuccessResponseDto {
    private Long memberId;
    private String nickname;
    private String role;
}
