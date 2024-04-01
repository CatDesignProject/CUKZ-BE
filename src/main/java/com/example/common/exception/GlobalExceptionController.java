package com.example.common.exception;

import com.example.common.global.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<BaseResponse<Object>> handleException(GlobalException e) {

        ExceptionResponseDto responseDto = new ExceptionResponseDto(e.getErrorCode());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(e.getErrorCode().getStatus(), responseDto));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidException(MethodArgumentNotValidException e) {

        List<ValidationErrorResponseDto> validationErrorResponseDtos = e.getAllErrors().stream().map
                (objectError -> new ValidationErrorResponseDto(objectError))
                .collect(Collectors.toList());

        ExceptionResponseDto responseDto = new ExceptionResponseDto(HttpStatus.BAD_REQUEST, validationErrorResponseDtos);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.of(HttpStatus.BAD_REQUEST, responseDto));
    }
}
