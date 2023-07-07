package org.zerock.moamoa.domain.DTO.user;

import lombok.Builder;
import lombok.Data;

@Data
public class UserPwUpdateRequest {
    private Long id;
    private String OldPw;
    private String NewPw;

    @Builder
    public UserPwUpdateRequest(Long id, String oldPw, String newPw) {
        this.id = id;
        this.OldPw = oldPw;
        this.NewPw = newPw;
    }
}
