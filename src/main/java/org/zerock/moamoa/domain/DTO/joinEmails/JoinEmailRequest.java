package org.zerock.moamoa.domain.DTO.joinEmails;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
public class JoinEmailRequest {
    private String email;
    private String token;
    private String code;
    private Boolean authenticate;

    @Builder
    public JoinEmailRequest(String email, String code, Boolean authenticate) {
        this.email = email;
        this.code = code;
        this.authenticate = authenticate;
    }
}
