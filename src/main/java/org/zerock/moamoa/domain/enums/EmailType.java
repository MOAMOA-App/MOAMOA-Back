package org.zerock.moamoa.domain.enums;

import lombok.Getter;

@Getter
public enum EmailType {
    EMAIL_JOIN(0),
    EMAIL_PW(1)
    ;

    EmailType(int code) {
        this.code = code;
    }

    private final int code;
}
