package com.example.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 1. 인증된 AuthenticatedMember 객체를 꺼낸다.
        AuthenticatedMember authenticatedMember = (AuthenticatedMember) authentication.getPrincipal();

        LoginSuccessResponseDto loginSuccessResponseDto = LoginSuccessResponseDto.builder()
                .memberId(authenticatedMember.getMemberId())
                .nickname(authenticatedMember.getNickName())
                .role(authenticatedMember.getRole().name())
                .build();

        LoginSuccessResponseDto responseDto = loginSuccessResponseDto;

        // 2. 응답한다.
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseDto));
        response.flushBuffer();
    }
}
