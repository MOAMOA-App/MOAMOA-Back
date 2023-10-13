package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserPwChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;

    @Builder
    public UserPwChangeRequest(String email, String oldPassword, String newPassword) {
        this.email = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
