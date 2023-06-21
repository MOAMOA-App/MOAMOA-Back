package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.User;

@Data
public class ProfileDTO {
    private Long id;

    private String nick;

    private String profImg;

    private String email;

    private String address;

    private String detailAddress;

    public ProfileDTO(User user) {
        id = user.getId();
        nick = user.getNick();
        profImg = user.getProfImg();
        email = user.getEmail();
        address = user.getAddress();
        detailAddress = user.getDetailAddress();
    }
}
