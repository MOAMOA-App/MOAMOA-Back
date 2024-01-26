package org.zerock.moamoa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.entity.Notice;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByReceiver(User receiverID, Pageable pageable);
    List<Notice> findByReceiver(User receiverID);

    default Notice findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOTICE_NOT_FOUND));
    }
}
