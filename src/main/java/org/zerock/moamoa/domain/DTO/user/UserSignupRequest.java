package org.zerock.moamoa.domain.DTO.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
public class UserSignupRequest {
    private String name;
    private String nick;
    private String naver;
    private String kakao;
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    private boolean activate;
    private Instant deletedAt;


    @Builder
    public UserSignupRequest(String nick, String email, String password) {
        this.nick = nick;
//        this.name = name;
        this.email = email;
        if (password != null) this.password = password;
        this.activate = true;
    }
}
