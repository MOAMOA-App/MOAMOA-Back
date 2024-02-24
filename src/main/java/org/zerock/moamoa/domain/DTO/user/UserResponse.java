package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.entity.User;

@Data
@NoArgsConstructor
public class UserResponse {
    private String code;
    private String nick;
    private String profImg;
    private String email;
    private String address;
    private String detailAddress;

    @Builder
    public UserResponse(String code, String nick, String profImg, String email, String address,
                        String detailAddress) {
        this.code = code;
        this.nick = nick;
        this.profImg = profImg;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public static UserResponse toDTO(User user) {
        UserResponse response = new UserResponse();
        response.code = user.getCode();
        response.nick = user.getNick();
        response.profImg = user.getProfImg();
        response.email = user.getEmail();
        response.address = user.getAddress();
        response.detailAddress = user.getDetailAddress();
        return response;
    }
}
