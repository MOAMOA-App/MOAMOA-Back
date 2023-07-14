package org.zerock.moamoa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Auth;
import org.zerock.moamoa.domain.entity.User;

public interface AuthRepository extends JpaRepository<Auth, Long> {
	Optional<Auth> findByRefreshToken(String refreshToken);

	Boolean existsByUser(User user);

	Optional<Auth> findByUserId(Long userId);
}
