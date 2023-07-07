package org.zerock.moamoa.domain.DTO.userblocked;

import lombok.Builder;
import lombok.Data;
import org.zerock.moamoa.domain.entity.User;

@Data
public class UserBlockedRequest {
    private Long id;

    User user;

    User target;

    @Builder
    public UserBlockedRequest(Long id, User user, User target) {
        this.id = id;
        this.user = user;
        this.target = target;
    }
}
