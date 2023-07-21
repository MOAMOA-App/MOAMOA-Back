package org.zerock.moamoa.domain.DTO.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
public class UserSignupRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    private String loginType;
    private boolean activate;

    @Builder
    public UserSignupRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.loginType = "email";
        this.activate = true;
    }
}
