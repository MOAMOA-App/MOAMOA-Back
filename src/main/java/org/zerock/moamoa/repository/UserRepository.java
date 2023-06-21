package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 주어진 이메일과 일치하는 사용자 반환
                                                // 존재하지 않는 경우 Optional.empty() 반환
    // 임시 주석처리
//    List<User> getMyUserParty(@Param(value = "id") Long id);    // id와 연관된 파티 (parties)에 참여하는 사용자를 조회
}
