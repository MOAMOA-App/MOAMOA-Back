package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.UserBlocked;

import java.util.List;

@Repository
public interface UserBlockedRepository extends JpaRepository<UserBlocked, Long> {

    List<UserBlocked> findAllByUser(User user);

    default UserBlocked findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
