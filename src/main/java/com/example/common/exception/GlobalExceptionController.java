package com.example.common.exception;

import com.example.common.global.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<BaseResponse<Object>> handleException(GlobalException e) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(e.getErrorCode());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(e.getErrorCode().getStatus(), responseDto));
    }
}
