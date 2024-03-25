package com.example.security.exception;

import com.example.common.exception.BaseErrorCode;
import com.example.common.exception.ExceptionResponseDto;
import com.example.common.global.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        BaseErrorCode errorCode = BaseErrorCode.FORBIDDEN_REQUEST;

        ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(errorCode);
        BaseResponse<ExceptionResponseDto> responseDto = BaseResponse.of(HttpStatus.FORBIDDEN, exceptionResponseDto);

        String responseJson = objectMapper.writeValueAsString(responseDto);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseJson);
    }
}
