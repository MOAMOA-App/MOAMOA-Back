package org.zerock.moamoa.domain.DTO;

import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.UserBlocked;

import java.time.LocalDateTime;

public class UserBlockedDTO {
    private Long id;

    User user;

    User target;

    private LocalDateTime createdAt;

    public UserBlockedDTO(UserBlocked userBlocked) {
        id = userBlocked.getId();
        user = userBlocked.getUser();
        target = userBlocked.getTarget();
        createdAt = userBlocked.getCreatedAt();
    }
}
