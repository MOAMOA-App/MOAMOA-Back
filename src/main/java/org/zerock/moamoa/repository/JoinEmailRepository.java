package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.JoinEmail;

import java.util.Optional;
@Repository
public interface JoinEmailRepository extends JpaRepository<JoinEmail, Long> {
    default JoinEmail findByTokenOrThrow(String token) {
        return findByToken(token)
                .orElseThrow(()-> new EntityNotFoundException(ErrorCode.INVALID_EMAIL_EXIST));
    }
    Optional<JoinEmail> findByToken(String token);
    Boolean existsByEmail(String email);
}
