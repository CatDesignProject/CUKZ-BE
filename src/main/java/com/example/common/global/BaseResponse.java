package com.example.common.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class BaseResponse<T> {
    private final HttpStatus status;
    private final T body;

    public static <T> BaseResponse<T> of(HttpStatus status, T body) {
        return new BaseResponse<>(status, body);
    }
}
