package org.zerock.moamoa.domain.DTO.email;

import lombok.Builder;
import lombok.Data;

@Data
public class EmailRequest {
    private String email;
    private String token;
    private String code;
    private Boolean authenticate;

    @Builder
    public EmailRequest(String email, String code, Boolean authenticate) {
        this.email = email;
        this.code = code;
        this.authenticate = authenticate;
    }
}
