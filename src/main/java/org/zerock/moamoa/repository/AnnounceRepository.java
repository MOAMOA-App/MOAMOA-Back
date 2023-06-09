package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Announce;
import org.zerock.moamoa.domain.entity.Product;

import java.util.List;


public interface AnnounceRepository extends JpaRepository<Announce, Long> {
}
