package org.zerock.moamoa.domain.DTO.email;

import lombok.Data;

@Data
public class EmailTokenResponse {
    String token;
    String msg;

    public static EmailTokenResponse toDto(String token, String msg) {
        EmailTokenResponse res = new EmailTokenResponse();
        res.token = token;
        res.msg = msg;
        return res;
    }

    public static EmailTokenResponse toDto(String msg){
        EmailTokenResponse res = new EmailTokenResponse();
        res.msg = msg;
        return res;
    }
}
