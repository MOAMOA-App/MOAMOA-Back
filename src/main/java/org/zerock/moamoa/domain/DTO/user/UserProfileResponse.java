package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zerock.moamoa.domain.entity.User;

@Data
@NoArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String nick;
    private String profImg;
    private String email;
    private String address;
    private String detailAddress;

    @Builder
    public UserProfileResponse(Long id, String nick, String profImg, String email, String address,
                               String detailAddress) {
        this.id = id;
        this.nick = nick;
        this.profImg = profImg;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }

    public static UserProfileResponse builder(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.id = user.getId();
        response.nick = user.getNick();
        response.profImg = user.getProfImg();
        response.email = user.getEmail();
        response.address = user.getAddress();
        response.detailAddress = user.getDetailAddress();
        return response;
    }
}
