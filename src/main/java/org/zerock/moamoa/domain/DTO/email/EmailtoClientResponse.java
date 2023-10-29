package org.zerock.moamoa.domain.DTO.email;

import lombok.Data;

@Data
public class EmailtoClientResponse {
    String token;
    String msg;

    public static EmailtoClientResponse toDto(String token, String msg) {
        EmailtoClientResponse res = new EmailtoClientResponse();
        res.token = token;
        res.msg = msg;
        return res;
    }

    public static EmailtoClientResponse toDto(String msg){
        EmailtoClientResponse res = new EmailtoClientResponse();
        res.msg = msg;
        return res;
    }
}
