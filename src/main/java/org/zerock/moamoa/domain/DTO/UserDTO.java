package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.User;

import javax.persistence.Column;

@Data
public class UserDTO {
    private Long id;

    private String name;

    private String password;

    private String phone;

    private String nick;

    private String profImg;

    private String email;

    private String address;

    private String detailAddress;

    public UserDTO(User user) {
        id = user.getId();
        name = user.getName();
        password = user.getPassword();
        phone = user.getPhone();
        nick = user.getNick();
        profImg = user.getProfImg();
        email = user.getEmail();
        address = user.getAddress();
        detailAddress = user.getDetailAddress();
    }
}
