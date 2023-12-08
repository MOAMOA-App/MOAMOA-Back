package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserCheckRequest {
    private String email;
    private String password;

    @Builder
    public UserCheckRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
