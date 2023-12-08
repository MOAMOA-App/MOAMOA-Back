package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String nick;
    private String profImg;
    private String address;
    private String detailAddress;

    @Builder
    public UserResponse(Long id, String name, String email, String nick, String profImg, String address, String detailAddress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.nick = nick;
        this.profImg = profImg;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
