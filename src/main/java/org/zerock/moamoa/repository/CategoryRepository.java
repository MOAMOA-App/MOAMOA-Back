package org.zerock.moamoa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.moamoa.domain.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
