package org.zerock.moamoa.common.user;

import lombok.*;

@Getter
@Setter
@Builder
public class EmailMessage {
    private String to;
    private String subject;
    private String message;
    @Builder.Default
    private String from = "moamoa-mail@naver.com";
}