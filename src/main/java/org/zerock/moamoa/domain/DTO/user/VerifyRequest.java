package org.zerock.moamoa.domain.DTO.user;

import lombok.Data;

@Data
public class VerifyRequest {
    String email;
    String password;

    public VerifyRequest() {
    }
}
