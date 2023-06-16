package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProductRepository  extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByUser(User user);

    Optional<Product> findById(Long id);
}