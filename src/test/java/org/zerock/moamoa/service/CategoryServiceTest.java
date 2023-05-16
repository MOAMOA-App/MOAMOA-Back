package org.zerock.moamoa.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.moamoa.domain.entity.Category;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.CategoryRepository;

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
        for(int i = 0 ; i < 10 ; i ++){
            String temp = Integer.toString(i);
            Category category = new Category();
            category.setName(temp);

            categoryService.saveCategory(category);
        }
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