package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginRequest {
    private String email;
    private String password;

    @Builder
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
