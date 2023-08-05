package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    default User findByEmailOrThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);   // 주어진 이메일과 일치하는 사용자 반환, 존재x-> Optional.empty() 반환

    Boolean existsByEmail(String email);

    // ist<User> getMyUserParty(@Param(value = "id") Long id);    // id와 연관된 파티 (parties)에 참여하는 사용자를 조회
}
