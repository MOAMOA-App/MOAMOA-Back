package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {
    private Long id;

    private String nick;

    private String profImg;

    private String email;

    private String address;

    private String detailAddress;

    @Builder
    public UserProfileUpdateRequest(Long id, String nick, String profImg, String email, String address,
                                    String detailAddress) {
        this.id = id;
        this.nick = nick;
        this.profImg = profImg;
        this.email = email;
        this.address = address;
        this.detailAddress = detailAddress;
    }
}
