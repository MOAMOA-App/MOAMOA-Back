package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.user.StringMaker;
import org.zerock.moamoa.domain.entity.User;

@Data
@NoArgsConstructor
public class UserProductResponse {
    private String code;
    private String nick;
    private String profImg;
    private String email;

    @Builder
    public UserProductResponse(String code, String nick, String profImg, String email) {
        this.code = code;
        this.nick = nick;
        this.profImg = profImg;
        this.email = email;
    }

    public static UserProductResponse toDto(User user){
        UserProductResponse res = new UserProductResponse();
        res.code = user.getCode();
        res.nick = user.getNick();
        res.email = user.getEmail();
        res.profImg = user.getProfImg();
        return res;
    }

}
