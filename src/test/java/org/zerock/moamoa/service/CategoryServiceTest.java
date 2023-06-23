package org.zerock.moamoa.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void saveCategory() {
        Category category = new Category();
        category.setName("Test Category");

        Category savedCategory = categoryService.saveCategory(category);

        // 저장된 카테고리의 ID를 확인할 수 있는지 테스트
        assertNotNull(savedCategory.getId());

        // 저장된 카테고리의 이름이 예상과 일치하는지 테스트
        assertEquals("Test Category", savedCategory.getName());

        // 실제 데이터베이스에서 해당 카테고리를 조회하여 확인
        Category retrievedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);
        assertNotNull(retrievedCategory);
        assertEquals("Test Category", retrievedCategory.getName());
    }

    @Test
    void findById() {
        // Given
        Category category = new Category();
        category.setName("Test Category");
        Category savedCategory = categoryRepository.save(category);

        // When
        Category foundCategory = categoryService.findById(savedCategory.getId());

        // Then
        assertEquals(savedCategory.getId(), foundCategory.getId());
        assertEquals(savedCategory.getName(), foundCategory.getName());
    }

    @Test
    void findAll() {
        System.out.println(categoryService.findAll());
    }

    @Test
    void deleteCategory() {
        Category category = new Category();
        category.setName("Test Category");

        Category createdCategory = categoryService.saveCategory(category);

        long categoryId = createdCategory.getId();
        categoryService.deleteCategory(categoryId);

        assertThrows(RuntimeException.class, () -> {
            categoryService.findById(categoryId);
        });
    }
}