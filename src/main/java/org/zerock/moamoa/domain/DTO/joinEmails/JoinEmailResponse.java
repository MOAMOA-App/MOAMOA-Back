package org.zerock.moamoa.domain.DTO.joinEmails;

import lombok.Data;
import org.zerock.moamoa.domain.entity.JoinEmail;

@Data
public class JoinEmailResponse {
    private Long id;

    private String email;

    private String token;

    private String code;

    private Boolean authenticate;

//    public JoinEmailResponse(String email, String code, Boolean authenticate) {
//        this.email = email;
//        this.code = code;
//        this.authenticate = authenticate;
//    }

    public static JoinEmailResponse toDto(JoinEmail joinEmail) {
        JoinEmailResponse res = new JoinEmailResponse();
        res.email = joinEmail.getEmail();
        res.token = joinEmail.getToken();
        res.code = joinEmail.getCode();
        res.authenticate = joinEmail.getAuthenticate();
        return res;
    }
}
