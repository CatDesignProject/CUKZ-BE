package com.example.security.exception;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.ExceptionResponseDto;
import com.example.common.global.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 1. 발생한 AuthenticationException에 대해 BaseErrorCode를 생성한다.
        BaseErrorCode errorCode = BaseErrorCode.BASE_SECURITY_EXCEPTION;

        // 2. BaseResponse 객체에 ExceptionResponseDTO를 담는다.
        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(errorCode);
        BaseResponse<ExceptionResponseDto> responseDto = BaseResponse.of(errorCode.getStatus(), exceptionResponseDto);

        // 3. ObjectMapper로 BaseResponse를 json으로 변환해 response에서 writer를 얻어 write한다.
        String responseJson = objectMapper.writeValueAsString(responseDto);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseJson);
    }
}
