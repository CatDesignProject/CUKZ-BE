package com.example.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRegisterRequestDto {

    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,15}$", message = "영문으로 시작, _ 포함 가능, 영문 숫자로 이뤄진 5~16자")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*])[A-Za-z\\d~!@#$%^&*]{8,16}$", message = "숫자, 문자, 특수문자(~,!,@,#,$,%,^,&,*) 포함 8~16자")
    private String password;
    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;

    @Builder
    public MemberRegisterRequestDto(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
    }
}
