package com.example.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum BaseErrorCode {

    // Member
    NOT_FOUND_MEMBER(NOT_FOUND, "사용자가 존재하지 않습니다."),
    BAD_CREDENTIAL(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_MEMBER(BAD_REQUEST, "이미 등록된 아이디입니다."),
    MISMATCHED_MEMBER(BAD_REQUEST, "작성자와 일치하지 않는 유저입니다."),
    UNAUTHORIZED_MEMBER(UNAUTHORIZED, "권한이 없는 유저입니다."),

    // Form
    NOT_FOUND_FORM(NOT_FOUND, "해당 폼을 찾을 수 없습니다."),
    DUPLICATED_FORM(BAD_REQUEST, "이미 참여한 폼이 있습니다."),
    NOT_IN_PERIOD(BAD_REQUEST, "참여 가능한 기간이 아닙니다."),
    DUPLICATED_PARTICIPATION(BAD_REQUEST, "이미 참여한 폼이 있습니다."),
    NOT_FOUND_DELIVERY(BAD_REQUEST, "해당 배송지를 찾을 수 없습니다."),

    // Product
    NOT_FOUND_PRODUCT(NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    NOT_FOUND_SEARCH_PRODUCT(NOT_FOUND, "검색 결과 해당 상품이 없습니다."),
    UNAUTHORIZED_MODIFY_PRODUCT(FORBIDDEN, "해당 상품을 수정할 권한이 없습니다."),
    UNAUTHORIZED_DELETE_PRODUCT(FORBIDDEN, "해당 상품을 삭제할 권한이 없습니다."),
    NOT_FOUND_PAGING_PRODUCT(NOT_FOUND, "목록에 상품이 없습니다."),
    INVALID_PERIOD(BAD_REQUEST, "시작일이 종료일보다 늦습니다."),
    INVALID_STATUS(BAD_REQUEST, "기간에 따른 상품 상태가 올바르지 않습니다."),

    // Option
    NOT_FOUND_OPTION(NOT_FOUND, "해당 옵션을 찾을 수 없습니다."),

    //Image
    NOT_FOUND_IMAGE(BAD_REQUEST, "해당 이미지를 찾을 수 없습니다."),

    // Security
    BASE_SECURITY_EXCEPTION(UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    FORBIDDEN_REQUEST(FORBIDDEN, "접근 권한이 없습니다."),

    // Likes
    DUPLICATED_LIKES(BAD_REQUEST, "이미 좋아요를 누른 상품입니다."),
    NOT_FOUND_LIKES(NOT_FOUND, "해당 좋아요를 찾을 수 없습니다."),

    // Review
    UNAUTHORIZED_WRITE_REVIEW_TO_SELLER(FORBIDDEN, "해당 총대에게 리뷰를 작성할 권한이 없습니다."),
    UNAUTHORIZED_WRITE_REVIEW_FROM_PURCHASE_FORM(FORBIDDEN, "해당 구매폼으로 리뷰를 작성할 권한이 없습니다."),
    ALREADY_WRITE_REVIEW(FORBIDDEN, "이미 리뷰를 작성했습니다."),
    NOT_YET_WRITE_REVIEW(FORBIDDEN, "아직 해당 상품은 리뷰 작성을 할 수 없습니다.");

    private final HttpStatus status;
    private final String msg;
}
