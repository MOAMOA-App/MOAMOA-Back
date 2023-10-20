package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProductResponse {
    private Long id;
    private String nick;
    private String profImg;
    private String email;

    @Builder
    public UserProductResponse(Long id, String nick, String profImg, String email) {
        this.id = id;
        this.nick = nick;
        this.profImg = profImg;
        this.email = email;
    }

}
