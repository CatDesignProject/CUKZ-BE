package com.example.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyUsernameDto {

    private String username;

    public VerifyUsernameDto(String username) {
        this.username= username;
    }
}
