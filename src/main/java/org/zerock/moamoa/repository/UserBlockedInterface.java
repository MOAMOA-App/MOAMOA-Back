package org.zerock.moamoa.repository;

import org.zerock.moamoa.domain.entity.UserBlocked;

import java.util.List;

public interface UserBlockedInterface {
    List<UserBlocked> findByUserList(Long UserId);
}
