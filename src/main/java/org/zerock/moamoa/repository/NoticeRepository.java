package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.Notice;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<Notice,Long> {
    List<Notice> findReminderNoticesByReceiverID(@Param("receiverId") Long receiverId);
    Optional<Notice> findByIdAndReceiverID(Long noticeId, Long receiverId);
}
