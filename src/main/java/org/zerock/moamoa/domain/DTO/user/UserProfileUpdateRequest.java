package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {

    private String nick;

    private String email;

    private String address;

    private String detailAddress;

    @Builder
    public UserProfileUpdateRequest(Long id, String nick, String email, String address,
                                    String detailAddress) {
        this.nick = nick;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
