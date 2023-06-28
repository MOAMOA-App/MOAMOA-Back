package org.zerock.moamoa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	List<Product> findByUser(User user);

	// List<Product> findBy
}
