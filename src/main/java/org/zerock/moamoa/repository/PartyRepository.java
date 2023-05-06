package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Party;


public interface PartyRepository extends JpaRepository<Party, Long> {
}
