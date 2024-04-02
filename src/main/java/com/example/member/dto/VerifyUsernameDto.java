package com.example.member.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyUsernameDto {

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$", message = "영문으로 시작, _ 포함 가능, 영문 숫자로 이뤄진 5~16자")
    private String username;

    public VerifyUsernameDto(String username) {
        this.username= username;
    }
}
