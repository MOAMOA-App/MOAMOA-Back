package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.User;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByRefreshToken(String refreshToken);

    Boolean existsByUser(User user);

    Optional<Auth> findByUserId(Long userId);
}
