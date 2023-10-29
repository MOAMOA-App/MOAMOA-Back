package org.zerock.moamoa.domain.DTO.email;

import lombok.Data;
import org.zerock.moamoa.domain.entity.Email;
import org.zerock.moamoa.domain.enums.EmailType;

@Data
public class EmailResponse {
    private Long id;

    private String email;

    private String token;

    private EmailType type;

    private String code;

    private Boolean authenticate;

//    public JoinEmailResponse(String email, String code, Boolean authenticate) {
//        this.email = email;
//        this.code = code;
//        this.authenticate = authenticate;
//    }

    public static EmailResponse toDto(Email email) {
        EmailResponse res = new EmailResponse();
        res.email = email.getEmail();
        res.type = email.getType();
        res.token = email.getToken();
        res.code = email.getCode();
        res.authenticate = email.getAuthenticate();
        return res;
    }
}
