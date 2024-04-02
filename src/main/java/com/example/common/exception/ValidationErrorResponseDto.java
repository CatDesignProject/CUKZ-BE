package com.example.common.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Getter
@NoArgsConstructor
public class ValidationErrorResponseDto {
    private String field;
    private String msg;

    public ValidationErrorResponseDto(ObjectError e) {
        this.field = ((FieldError) e).getField();
        this.msg = e.getDefaultMessage();
    }
}
