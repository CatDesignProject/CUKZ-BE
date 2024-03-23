package com.example.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum BaseErrorCode {

    // Member
    NOT_FOUND_MEMBER(NOT_FOUND, "아이디가 일치하지 않습니다."),
    BAD_CREDENTIAL(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_MEMBER(BAD_REQUEST, "이미 등록된 아이디입니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "권한이 없는 유저입니다."),

    // Form
    NOT_FOUND_FORM(NOT_FOUND, "해당 폼을 찾을 수 없습니다."),
    DUPLICATED_FORM(BAD_REQUEST, "이미 참여한 폼이 있습니다."),
    NOT_IN_PERIOD(BAD_REQUEST, "참여 가능한 기간이 아닙니다."),

    // Product
    NOT_FOUND_PRODUCT(NOT_FOUND, "해당 상품을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
