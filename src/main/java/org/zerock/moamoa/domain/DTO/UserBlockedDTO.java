package org.zerock.moamoa.domain.DTO;

import lombok.Data;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.UserBlocked;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class UserBlockedDTO {
    private Long id;

    User user;

    User target;

    private Instant createdAt;

    public UserBlockedDTO(UserBlocked userBlocked) {
        id = userBlocked.getId();
        user = userBlocked.getUser();
        target = userBlocked.getTarget();
        createdAt = userBlocked.getCreatedAt();
    }
}
