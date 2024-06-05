package com.example.common.mail;

import lombok.Getter;

@Getter
public enum MailType {
    VERIFY_EMAIL("verify-email","[CUKZ] 총대 신청 인증 메일"),
    CHANGE_PASSWORD("change-password", "[CUKZ] 비밀번호 변경 인증 메일");

    private String path;
    private String title;

    MailType(String path, String title) {
        this.path = path;
        this.title = title;
    }
}
