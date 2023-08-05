package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.ProductImages;

public interface ProductImageRepository extends JpaRepository<ProductImages, Long> {
}
