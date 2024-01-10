package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.common.user.StringMaker;

@Data
public class UserNickResponse {
    private String code;
    private String nick;

    @Builder
    public UserNickResponse(String code, String nick) {
        this.code = code;
        this.nick = nick;
    }
}
