package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.UserBlocked;


public interface UserBlockedRepository extends JpaRepository<UserBlocked, Long>, UserBlockedInterface {
}
