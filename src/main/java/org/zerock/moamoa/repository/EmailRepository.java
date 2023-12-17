package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.Email;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    default Email findByTokenOrThrow(String token) {
        return findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INVALID_EMAIL_VALUE));
    }

    Optional<Email> findByToken(String token);

    default Email findByEmailOrThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.INVALID_EMAIL_VALUE));
    }

    Optional<Email> findByEmail(String email);
    
    Boolean existsByEmail(String email);

    List<Email> findAllByUpdatedAtBefore(Instant nowDate);
}
