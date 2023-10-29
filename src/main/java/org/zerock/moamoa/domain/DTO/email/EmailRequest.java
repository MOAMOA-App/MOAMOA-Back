package org.zerock.moamoa.domain.DTO.email;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.enums.EmailType;

@Data
public class EmailRequest {
    private String email;
    private String token;
    private EmailType type;
    private String code;
    private Boolean authenticate;

    @Builder
    public EmailRequest(String email, String code, EmailType type) {
        this.email = email;
        this.code = code;
        this.type = type;
        this.authenticate = false;
    }
}
