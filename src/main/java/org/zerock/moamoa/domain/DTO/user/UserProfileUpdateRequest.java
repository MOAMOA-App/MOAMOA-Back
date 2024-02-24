package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {

    private String email;
    private String nick;
    private String address;
    private String detailAddress;

    @Builder
    public UserProfileUpdateRequest(String email, String nick, String address, String detailAddress) {
        this.email = email;
        this.nick = nick;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
