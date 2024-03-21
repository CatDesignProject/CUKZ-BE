package com.example.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public LoginProcessingFilter() {
        super(new AntPathRequestMatcher("/members/login")); // "/members/login" 요청에 Filter를 적용
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 1. HTTP 요청 바디 json을 읽어 DTO로 변환
        UsernamePasswordLoginDto usernamePasswordLoginDto = jsonToUsernamePasswordLoginDto(request);

        // 2. 인증처리 전의 Authentication 객체를 생성
        MemberAuthenticationToken authRequest =
                MemberAuthenticationToken.unauthenticated(usernamePasswordLoginDto);

        // 3. AuthenticationManager에게 인증처리를 위임
        return super.getAuthenticationManager().authenticate(authRequest);
    }

    private UsernamePasswordLoginDto jsonToUsernamePasswordLoginDto(HttpServletRequest request) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String usernamePasswordJson = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(usernamePasswordJson, UsernamePasswordLoginDto.class);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        // 1. 비어있는 SecurityContext를 생성
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // 2. 인증처리 완료된 Authentication 객체를 SecurityContext에 등록
        context.setAuthentication(authResult);

        this.securityContextRepository.saveContext(context, request, response);
    }

}