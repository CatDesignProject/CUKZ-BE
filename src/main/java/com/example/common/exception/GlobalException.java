package com.example.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public GlobalException(BaseErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
}
