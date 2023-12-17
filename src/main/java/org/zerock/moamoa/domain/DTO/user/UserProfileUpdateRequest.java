package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {

    private String email;
    private String nick;
    private String address;
    private String detailAddress;
}
