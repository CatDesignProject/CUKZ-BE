package com.example.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class ExceptionResponseDto {

    private final HttpStatus status;
    private final Object msg;

    public ExceptionResponseDto(BaseErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.msg = errorCode.getMsg();
    }

    public ExceptionResponseDto(HttpStatus status, List<ValidationErrorResponseDto> validationErrors) {
        this.status = status;
        this.msg = validationErrors;
    }
}
