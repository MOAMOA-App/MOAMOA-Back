package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.entity.User;

@Data
public class UserNickResponse {
    private Long id;
    private String nick;

    @Builder
    public UserNickResponse(Long id, String nick) {
        this.id = id;
        this.nick = nick;
    }
}
