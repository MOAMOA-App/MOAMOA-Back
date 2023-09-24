package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.Announce;

@Repository
public interface AnnounceRepository extends JpaRepository<Announce, Long> {

    default Announce findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ANNOUNCE_NOT_FOUND));
    }
}
