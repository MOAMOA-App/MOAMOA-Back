package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);   // 주어진 이메일과 일치하는 사용자 반환, 존재x-> Optional.empty() 반환

//    Optional<User> findByUID(Long uid);
}
