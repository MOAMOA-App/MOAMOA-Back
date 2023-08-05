package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.moamoa.domain.entity.Announce;

@Repository
public interface AnnounceRepository extends JpaRepository<Announce, Long> {
    void deleteById(Long id);
}
