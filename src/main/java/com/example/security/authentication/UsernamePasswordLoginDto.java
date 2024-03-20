package com.example.security.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UsernamePasswordLoginDto {
    private String username;
    private String password;

    @Builder
    public UsernamePasswordLoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
