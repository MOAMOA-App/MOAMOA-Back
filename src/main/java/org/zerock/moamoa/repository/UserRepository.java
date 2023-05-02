package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.User;

public interface UserRepository  extends JpaRepository<User, Long> {

}
