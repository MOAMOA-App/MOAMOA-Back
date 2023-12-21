package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.common.domain.entity.BaseEntity;
import org.zerock.moamoa.domain.entity.User;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String nick;
    private String profImg;
    private String email;
    private String address;
    private String detailAddress;
    private String uuid;

    @Builder
    public UserResponse(Long id, String nick, String profImg, String email, String address,
                        String detailAddress) {
        this.id = id;
        this.nick = nick;
        this.profImg = profImg;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public static UserResponse builder(User user) {
        UserResponse response = new UserResponse();
        response.id = user.getId();
        response.nick = user.getNick();
        response.profImg = user.getProfImg();
        response.email = user.getEmail();
        response.address = user.getAddress();
        response.detailAddress = user.getDetailAddress();
        return response;
    }
}
