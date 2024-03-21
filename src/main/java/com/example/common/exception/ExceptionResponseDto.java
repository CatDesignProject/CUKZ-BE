package com.example.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponseDto {

    private final HttpStatus status;
    private final String msg;

    public ExceptionResponseDto(BaseErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.msg = errorCode.getMsg();
    }
}
