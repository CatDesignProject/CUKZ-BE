package com.example.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VerifyMailDto {

    @Email
    private String email;

    public VerifyMailDto(String email) {
        this.email = email;
    }
}
