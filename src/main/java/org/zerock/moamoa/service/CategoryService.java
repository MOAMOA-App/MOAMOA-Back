package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.Category;
import org.zerock.moamoa.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category findById(long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()) {
            return category.get();
        }
        throw new RuntimeException("Category not found with id " + categoryId);
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public void deleteCategory(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
