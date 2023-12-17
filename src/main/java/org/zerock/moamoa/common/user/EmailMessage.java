package org.zerock.moamoa.common.user;

import lombok.*;

@Getter
@Setter
public class EmailMessage {
    private String to;
    private String subject = "모아모아에서 발급된 이메일 인증 코드입니다.";
    private String from = "moamoa-mail@naver.com";

    @Builder
    public EmailMessage(String to) {
        this.to = to;
    }
}