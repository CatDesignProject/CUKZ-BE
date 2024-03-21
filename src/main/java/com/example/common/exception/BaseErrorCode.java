package com.example.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BaseErrorCode {

    // Member
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "아이디가 일치하지 않습니다."),
    BAD_CREDENTIAL(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_MEMBER(HttpStatus.BAD_REQUEST, "이미 등록된 아이디입니다."),

    // Form
    NOT_FOUND_FORM(HttpStatus.BAD_REQUEST, "해당 폼을 찾을 수 없습니다."),

    // Product
    NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "해당 상품을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
