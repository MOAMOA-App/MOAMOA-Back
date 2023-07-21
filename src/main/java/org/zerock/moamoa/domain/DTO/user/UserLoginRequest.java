package org.zerock.moamoa.domain.DTO.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @Builder
    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
