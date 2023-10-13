package org.zerock.moamoa.domain.DTO.email;

import lombok.Builder;
import lombok.Data;

@Data
public class EmailUserPwRequest {
    private String token;
    private String password;

    @Builder
    public EmailUserPwRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }
}
