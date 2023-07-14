package org.zerock.moamoa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.Notice;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
