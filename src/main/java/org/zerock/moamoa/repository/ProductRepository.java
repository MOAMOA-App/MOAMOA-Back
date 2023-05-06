package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Product;

public interface ProductRepository  extends JpaRepository<Product, Long> {

}
