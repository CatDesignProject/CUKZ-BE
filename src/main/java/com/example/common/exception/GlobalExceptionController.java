package com.example.common.exception;

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
    public ResponseEntity<Object> handleException(GlobalException e) {

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidException(MethodArgumentNotValidException e) {

        List<ValidationErrorResponseDto> validationErrorResponseDtos = e.getAllErrors().stream().map
                        (objectError -> new ValidationErrorResponseDto(objectError))
                .collect(Collectors.toList());

        ExceptionResponseDto responseDto = new ExceptionResponseDto(HttpStatus.BAD_REQUEST, validationErrorResponseDtos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }
}
