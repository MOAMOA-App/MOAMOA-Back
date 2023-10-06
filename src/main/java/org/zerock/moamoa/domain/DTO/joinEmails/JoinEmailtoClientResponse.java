package org.zerock.moamoa.domain.DTO.joinEmails;

import lombok.Data;

@Data
public class JoinEmailtoClientResponse {
    String token;
    String msg;

    public static JoinEmailtoClientResponse toDto(String token, String msg) {
        JoinEmailtoClientResponse res = new JoinEmailtoClientResponse();
        res.token = token;
        res.msg = msg;
        return res;
    }

    public static JoinEmailtoClientResponse toDto(String msg) {
        JoinEmailtoClientResponse res = new JoinEmailtoClientResponse();
        res.msg = msg;
        return res;
    }
}
