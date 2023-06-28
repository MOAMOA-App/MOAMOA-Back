package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Announce;

public interface AnnounceRepository extends JpaRepository<Announce, Long> {
	void deleteById(Long id);
}
