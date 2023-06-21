package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.User;

import java.util.List;


public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findByBuyer(User buyer);
}
