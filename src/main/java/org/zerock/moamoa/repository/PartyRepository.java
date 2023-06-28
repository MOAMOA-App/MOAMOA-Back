package org.zerock.moamoa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

public interface PartyRepository extends JpaRepository<Party, Long> {
	List<Party> findByBuyer(User buyer);

	List<Party> findByProduct(Product product);
}
